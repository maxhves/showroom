package no.mhl.showroom.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.TypedArray
import android.graphics.Color
import android.graphics.Typeface
import android.os.Build
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import coil.Coil
import coil.ImageLoader
import coil.util.CoilUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import no.mhl.showroom.Constants.ANIM_DURATION
import no.mhl.showroom.Constants.MAX_ALPHA
import no.mhl.showroom.Constants.MIN_ALPHA
import no.mhl.showroom.Constants.TRANSPARENT
import no.mhl.showroom.R
import no.mhl.showroom.data.preloadUpcomingImages
import no.mhl.showroom.model.GalleryImage
import no.mhl.showroom.ui.adapter.ImagePagerAdapter
import no.mhl.showroom.ui.adapter.ThumbnailRecyclerAdapter
import no.mhl.showroom.ui.views.InfiniteViewPager2
import no.mhl.showroom.util.setCount
import no.mhl.showroom.util.setDescription
import okhttp3.OkHttpClient


class  Showroom
constructor(context: Context, attrs: AttributeSet?) : FrameLayout(context, attrs) {

    // region View Properties
    private lateinit var parentActivity: AppCompatActivity
    private val imageViewPager by lazy { findViewById<InfiniteViewPager2>(R.id.image_recycler) }
    private val thumbnailRecycler by lazy { findViewById<RecyclerView>(R.id.thumbnail_recycler) }
    private val thumbnailRecyclerContainer by lazy { findViewById<ConstraintLayout>(R.id.thumbnail_recycler_container) }
    private val toolbar by lazy { findViewById<Toolbar>(R.id.toolbar) }
    private val descriptionText by lazy { findViewById<TextView>(R.id.description) }
    private val countText by lazy { findViewById<TextView>(R.id.image_counter) }
    // endregion

    // region Data Properties
    private lateinit var galleryData: List<GalleryImage>
    private lateinit var imagePagerAdapter: ImagePagerAdapter
    private lateinit var thumbnailRecyclerAdapter: ThumbnailRecyclerAdapter
    private var originalStatusBarColor: Int = 0
    private var originalNavigationBarColor: Int = 0
    private var originalStatusBarsAreLight: Boolean = false
    private var initialPosition: Int = 0
    private var immersed: Boolean = false
    private var topPaddingUpdated: Boolean = false
    private var bottomPaddingUpdated: Boolean = false
    // endregion

    // region Custom Attributes
    private var fontPrimary: Typeface? = Typeface.DEFAULT
    private var fontSecondary: Typeface? = Typeface.DEFAULT
    private var imagePreloadLimit: Int = 3
    // endregion

    // region IO Event Properties
    private var onBackNavigationPressed: ((position: Int) -> Unit)? = null
    // endregion

    // region Initialisation
    init {
        LayoutInflater.from(context).inflate(R.layout.layout_showroom, this)

        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.Showroom,
            0, 0
        ).apply(::setupCustomAttributes)

        setupCoil()
    }

    private fun setupCoil() {
        Coil.setDefaultImageLoader {
            ImageLoader(context) {
                crossfade(true)
                okHttpClient {
                    OkHttpClient.Builder()
                        .cache(CoilUtils.createDefaultCache(context))
                        .build()
                }
            }
        }
    }
    // endregion

    // region View Setup
    private fun setupCustomAttributes(typedArray: TypedArray) {
        try {
            val fontPrimaryRes = typedArray.getResourceId(R.styleable.Showroom_fontFamilyPrimary, 0)
            val fontSecondaryRes =
                typedArray.getResourceId(R.styleable.Showroom_fontFamilySecondary, 0)
            val preloadLimit = typedArray.getInteger(R.styleable.Showroom_imagePreloadLimit, 3)

            if (fontPrimaryRes != 0) {
                fontPrimary = ResourcesCompat.getFont(context, fontPrimaryRes)
            }

            if (fontSecondaryRes != 0) {
                fontSecondary = ResourcesCompat.getFont(context, fontSecondaryRes)
            }

            descriptionText.typeface = fontPrimary
            countText.typeface = fontSecondary
            imagePreloadLimit = preloadLimit
        } finally {
            typedArray.recycle()
        }
    }

    fun attach(activity: AppCompatActivity, data: List<GalleryImage>, openAtIndex: Int = 0) {
        parentActivity = activity
        galleryData = data
        initialPosition = openAtIndex
        originalStatusBarColor = activity.window.statusBarColor
        originalNavigationBarColor = activity.window.navigationBarColor
        originalStatusBarsAreLight = isStatusBarLight()
        setupViews()
    }

    private fun setupViews() {
        setupEdgeToEdge()
        setupImageViewPager()
        setupThumbnailRecycler()
        setupToolbar()
    }

    private fun setupEdgeToEdge() {

        if (Build.VERSION.SDK_INT >= 28) {

            // Ensure system bar icons are white
            setStatusBarIconsLight()

            // Declare we are drawing under system bars
            WindowCompat.setDecorFitsSystemWindows(parentActivity.window, false)
            
            // Set system bars as translucent
            parentActivity.window.apply {
                navigationBarColor = Color.parseColor(TRANSPARENT)
                statusBarColor = Color.parseColor(TRANSPARENT)
            }

            // Listen for insets and adjust as necessary
            ViewCompat.setOnApplyWindowInsetsListener(toolbar) { v, insets ->
                if (topPaddingUpdated.not()) {
                    val systemInsets = insets.getInsets(WindowInsetsCompat.Type.displayCutout())
                    v.updatePadding(top = systemInsets.top)
                    topPaddingUpdated = true
                }
                insets
            }

            ViewCompat.setOnApplyWindowInsetsListener(thumbnailRecyclerContainer) { v, insets ->
                if (bottomPaddingUpdated.not()) {
                    val systemInsets = insets.getInsets(WindowInsetsCompat.Type.navigationBars())
                    v.updatePadding(bottom = systemInsets.bottom)
                    bottomPaddingUpdated = true
                }
                insets
            }

        } else {

            // On older SDK versions we simply set the system bars to black
            parentActivity.window.apply {
                navigationBarColor = Color.BLACK
                statusBarColor = Color.BLACK
            }

        }

    }

    private fun setupImageViewPager() {
        imagePagerAdapter = ImagePagerAdapter(galleryData)
        imagePagerAdapter.onImageClicked = {
            toggleImmersion()
            immersed = immersed.not()
            toggleGalleryUi(immersed)
        }

        imageViewPager.apply {
            setAdapter(imagePagerAdapter, imagePreloadLimit)
            currentItemPosition = initialPosition
            registerOnPageCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    val stablePosition = imageViewPager.currentItemPosition

                    thumbnailRecycler.smoothScrollToPosition(stablePosition)
                    setThumbnailAsSelected(stablePosition)
                    preload(stablePosition)
                }
            })
        }

    }

    private fun preload(position: Int) = CoroutineScope(Dispatchers.IO).launch {
        preloadUpcomingImages(context, position, galleryData, imagePreloadLimit)
    }

    private fun setupThumbnailRecycler() {
        thumbnailRecyclerAdapter = ThumbnailRecyclerAdapter(galleryData)
        thumbnailRecycler.apply {
            setHasFixedSize(true)
            adapter = thumbnailRecyclerAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            setItemViewCacheSize(imagePreloadLimit)
        }
        thumbnailRecyclerAdapter.onThumbnailClicked = { position ->
            imageViewPager.currentItemPosition = position
            setThumbnailAsSelected(position)
        }

        setThumbnailAsSelected(initialPosition)
    }

    private fun setupToolbar() {
        parentActivity.setSupportActionBar(toolbar)
        parentActivity.supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowTitleEnabled(false)
            setHomeAsUpIndicator(R.drawable.ic_clear)
        }

        toolbar.setNavigationOnClickListener {
            restoreWindowPreGallery()
            onBackNavigationPressed?.invoke(galleryData.indexOf(galleryData.first { it.selected }))
        }
    }
    // endregion

    // region Show/Hide System UI
    private fun toggleGalleryUi(hide: Boolean) {
        fun fade(view: View) {
            ViewCompat
                .animate(view)
                .alpha(if (hide) MIN_ALPHA else MAX_ALPHA)
                .setDuration(ANIM_DURATION)
                .withStartAction {
                    if (hide.not()) {
                        view.visibility = View.VISIBLE
                    }
                }
                .withEndAction {
                    if (hide) {
                        view.visibility = View.GONE
                    }
                }
                .start()
        }

        fun translateY(view: View) {
            ViewCompat
                .animate(view)
                .translationY(if (hide) view.height.toFloat() else 0f)
                .setDuration(ANIM_DURATION)
                .start()
        }

        fade(toolbar)
        fade(descriptionText)
        fade(countText)
        translateY(thumbnailRecyclerContainer)
    }

    private fun toggleImmersion() {
        // Only toggle system bars on SDK 28 and over to avoid graphical issues
        if (Build.VERSION.SDK_INT >= 28) {

            // Get the window insets controller
            val insetsController =
                WindowInsetsControllerCompat(parentActivity.window, parentActivity.window.decorView)

            // Set behavior of the immersive mode
            val behavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

            // Set type of system bars to hide and show
            val type = WindowInsetsCompat.Type.systemBars()
            insetsController.systemBarsBehavior = behavior

            // Toggle immersion
            if (immersed) {
                insetsController.show(type)
            } else {
                insetsController.hide(type)
            }
        }

    }

    // endregion

    // region Thumbnail Selection
    private fun setThumbnailAsSelected(position: Int) {
        val currentPosition = galleryData.indexOf(galleryData.find { it.selected })

        if (currentPosition != position) {
            if (position <= galleryData.size && position >= 0) {
                countText.setCount(position, galleryData.size)
                descriptionText.setDescription(galleryData[position].description)

                galleryData.find { it.selected }?.selected = false
                galleryData[position].selected = true

                thumbnailRecyclerAdapter.notifyDataSetChanged()
            }
        }
    }
    // endregion

    // region Miscellaneous
    private fun setStatusBarIconsDark() {
        val window = parentActivity.window
        WindowInsetsControllerCompat(window, window.decorView).apply {
            isAppearanceLightStatusBars = true
        }
    }

    private fun setStatusBarIconsLight() {
        val window = parentActivity.window
        WindowInsetsControllerCompat(window, window.decorView).apply {
            isAppearanceLightStatusBars = false
        }
    }

    @SuppressLint("InlinedApi")
    @Suppress("DEPRECATION")
    private fun isStatusBarLight(): Boolean {
        val window = parentActivity.window
        return window.decorView.systemUiVisibility and SYSTEM_UI_FLAG_LIGHT_STATUS_BAR != 0
    }

    fun restoreWindowPreGallery() {
        if (originalStatusBarsAreLight) { setStatusBarIconsDark() }

        parentActivity.window.apply {
            navigationBarColor = originalNavigationBarColor
            statusBarColor = originalStatusBarColor
        }

        if (immersed) { toggleImmersion() }
        WindowCompat.setDecorFitsSystemWindows(parentActivity.window, true)
    }
    // endregion

    // region IO Events
    fun setNavigationExitEvent(backPressedEvent: ((position: Int) -> Unit)) {
        onBackNavigationPressed = backPressedEvent
    }
    // endregion

}