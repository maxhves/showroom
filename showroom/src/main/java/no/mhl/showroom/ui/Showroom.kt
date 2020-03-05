package no.mhl.showroom.ui

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.updatePadding
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import coil.Coil
import coil.ImageLoader
import coil.api.load
import coil.util.CoilUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import no.mhl.showroom.data.model.GalleryData
import no.mhl.showroom.ui.adapter.ImagePagerAdapter
import no.mhl.showroom.ui.adapter.ThumbnailRecyclerAdapter
import no.mhl.showroom.R
import no.mhl.showroom.util.ShowRoomActivityUtils
import okhttp3.OkHttpClient

// region Static Constants
private const val ANIM_DURATION: Long = 250L
private const val MAX_ALPHA: Float = 1f
private const val MIN_ALPHA: Float = 0f
private const val PRELOAD_IMAGE_LIMIT: Int = 3
// endregion

class Showroom
constructor(context: Context, attrs: AttributeSet?) : FrameLayout(context, attrs) {

    // region Properties
    private lateinit var parentActivity: AppCompatActivity
    private val viewParent by lazy { findViewById<ConstraintLayout>(R.id.parent_view) }
    private val imageViewPager by lazy { findViewById<ViewPager2>(R.id.image_recycler) }
    private val thumbnailRecycler by lazy { findViewById<RecyclerView>(R.id.thumbnail_recycler) }
    private val thumbnailRecyclerContainer by lazy { findViewById<ConstraintLayout>(R.id.thumbnail_recycler_container) }
    private val toolbar: Toolbar by lazy { findViewById<Toolbar>(R.id.toolbar) }
    private val description by lazy { findViewById<TextView>(R.id.description) }
    private val counter by lazy { findViewById<TextView>(R.id.image_counter) }
    // endregion

    // region Data Properties
    private lateinit var galleryData: List<GalleryData>
    private lateinit var imagePagerAdapter: ImagePagerAdapter
    private lateinit var thumbnailRecyclerAdapter: ThumbnailRecyclerAdapter
    private var initialPosition: Int = 0
    private var isFullscreen: Boolean = false
    // endregion

    // region IO Event Properties
    private var onBackNavigationPressed: ((position: Int) -> Unit)? = null
    // endregion

    // region Initialisation
    init {
        LayoutInflater.from(context).inflate(R.layout.layout_gallery_image_showroom, this)
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
    fun attach(activity: AppCompatActivity, data: List<GalleryData>, openAtIndex: Int = 0) {
        parentActivity = activity
        galleryData = data
        initialPosition = openAtIndex - 1
        setupViews()
    }

    private fun setupViews() {
        setupLayoutDisplayCutoutMode()
        setupEdgeToEdge()
        setupImageViewPager()
        setupThumbnailRecycler()
        setupToolbar()
        setInitialPositionIfApplicable()
    }

    private fun setupLayoutDisplayCutoutMode() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            parentActivity.window.attributes.layoutInDisplayCutoutMode =
                WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
        }
    }

    private fun setupEdgeToEdge() {
        viewParent.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        parentActivity.window.apply {
            navigationBarColor = Color.TRANSPARENT
            statusBarColor = Color.TRANSPARENT
        }

        ViewCompat.setOnApplyWindowInsetsListener(toolbar) { v, insets ->
            v.updatePadding(top = insets.systemWindowInsetTop)
            insets
        }

        ViewCompat.setOnApplyWindowInsetsListener(thumbnailRecyclerContainer) { v, insets ->
            v.updatePadding(bottom = insets.systemWindowInsetBottom)
            insets
        }
    }

    private fun setupImageViewPager() {
        imagePagerAdapter = ImagePagerAdapter(galleryData)
        imageViewPager.apply {
            adapter = imagePagerAdapter
            offscreenPageLimit = PRELOAD_IMAGE_LIMIT.toInt()
        }

        imagePagerAdapter.onImageClicked = {
            setSystemUi(isFullscreen.not())
            isFullscreen = isFullscreen.not()
        }

        imageViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                thumbnailRecycler.smoothScrollToPosition(position)
                setThumbnailAsSelected(position)

                CoroutineScope(Dispatchers.IO).launch { preloadUpcomingImages(position) }
            }
        })
    }

    private suspend fun preloadUpcomingImages(position: Int) = withContext(Dispatchers.IO) {
        when (position) {
            0 -> {
                for (i in 0..PRELOAD_IMAGE_LIMIT) {
                    if (position + i < galleryData.size - 1) {
                        Coil.load(context, galleryData[position + i].image)
                        Coil.load(context, galleryData[position + i].downscaledImage)
                    }
                }
            }
            else ->  {
                if (position + 1 < galleryData.size - 1) {
                    Coil.load(context, galleryData[position + 1].image)
                    Coil.load(context, galleryData[position + 1].downscaledImage)
                }
            }
        }
    }

    private fun setupThumbnailRecycler() {
        thumbnailRecyclerAdapter = ThumbnailRecyclerAdapter(galleryData)
        thumbnailRecycler.apply {
            setHasFixedSize(true)
            adapter = thumbnailRecyclerAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            setItemViewCacheSize(PRELOAD_IMAGE_LIMIT * 2)
        }
        thumbnailRecyclerAdapter.onThumbnailClicked = { position ->
            imageViewPager.setCurrentItem(position, false)
            setThumbnailAsSelected(position)
        }

        setThumbnailAsSelected(0)
    }

    private fun setInitialPositionIfApplicable() {
        if (initialPosition > 0 && initialPosition < galleryData.size) {
            imageViewPager.setCurrentItem(initialPosition, false)
            thumbnailRecycler.scrollToPosition(initialPosition)
            setThumbnailAsSelected(initialPosition)
        }
    }

    private fun setupToolbar() {
        parentActivity.setSupportActionBar(toolbar)
        parentActivity.supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowTitleEnabled(false)
            setHomeAsUpIndicator(R.drawable.ic_clear)
        }

        toolbar.setNavigationOnClickListener {
            onBackNavigationPressed?.invoke(galleryData.indexOf(galleryData.first { it.selected }))
        }
    }
    // endregion

    // region Show/Hide System UI
    private fun setSystemUi(hide: Boolean) {
        ShowRoomActivityUtils.setSystemUiForActivity(parentActivity, hide)

        ViewCompat
            .animate(toolbar)
            .alpha(if (hide) MIN_ALPHA else MAX_ALPHA)
            .setDuration(ANIM_DURATION)
            .start()

        ViewCompat
            .animate(description)
            .alpha(if (hide) MIN_ALPHA else MAX_ALPHA)
            .setDuration(ANIM_DURATION)
            .start()

        ViewCompat
            .animate(counter)
            .alpha(if (hide) MIN_ALPHA else MAX_ALPHA)
            .setDuration(ANIM_DURATION)
            .start()

        ViewCompat
            .animate(thumbnailRecyclerContainer)
            .translationY(if (hide) thumbnailRecyclerContainer.height.toFloat() else 0f)
            .setDuration(ANIM_DURATION)
            .start()
    }
    // endregion

    // region Thumbnail Selection
    private fun setThumbnailAsSelected(position: Int) {
        val currentPosition = galleryData.indexOf(galleryData.find { it.selected })

        if (currentPosition != position) {
            if (position <= galleryData.size && position >= 0) {
                updateCounter(position)
                updateDescription(position)

                galleryData.find { it.selected }?.selected = false
                galleryData[position].selected = true

                thumbnailRecyclerAdapter.notifyDataSetChanged()
            }
        }
    }
    // endregion

    // region Counter Updates
    @SuppressWarnings("SetTextI18n")
    private fun updateCounter(position: Int) {
        counter.text = "(${position + 1}/${galleryData.size})"
    }
    // endregion

    // region Description Updates
    private fun updateDescription(position: Int) {
        val text = galleryData[position].description

        description.visibility = if (text.isNullOrBlank()) View.INVISIBLE else View.VISIBLE
        description.text = text
    }
    // endregion

    // region IO Events
    fun setBackPressedEvent(backPressedEvent: ((position: Int) -> Unit)) {
        onBackNavigationPressed = backPressedEvent
    }
    // endregion

}