package no.finn.granite.ui

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
import coil.util.CoilUtils
import no.finn.granite.R
import no.finn.granite.data.model.GalleryData
import no.finn.granite.ui.adapter.ImagePagerAdapter
import no.finn.granite.ui.adapter.ThumbnailRecyclerAdapter
import no.finn.granite.util.GraniteActivityUtils
import okhttp3.OkHttpClient

class GraniteImageGallery
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
    private var isFullscreen: Boolean = false
    // endregion

    // region IO Event Properties
    private var onBackNavigationPressed: ((position: Int) -> Unit)? = null
    // endregion

    // region Initialisation
    init {
        LayoutInflater.from(context).inflate(R.layout.layout_gallery_image_granite, this)
    }
    // endregion

    // region View Setup
    fun attachToActivity(activity: AppCompatActivity, data: List<GalleryData>) {
        parentActivity = activity
        galleryData = data
        setupViews()
    }

    private fun setupViews() {
        setupLayoutDisplayCutoutMode()
        setupEdgeToEdge()
        setupImageViewPager()
        setupThumbnailRecycler()
        setupToolbar()
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
        }

        imagePagerAdapter.onImageClicked = {
            setSystemUi(isFullscreen.not())
            isFullscreen = isFullscreen.not()
        }

        imageViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                thumbnailRecycler.smoothScrollToPosition(position)
                setThumbnailAsSelected(position)
            }
        })
    }

    private fun setupThumbnailRecycler() {
        thumbnailRecyclerAdapter = ThumbnailRecyclerAdapter(galleryData)
        thumbnailRecycler.apply {
            setHasFixedSize(true)
            adapter = thumbnailRecyclerAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }
        thumbnailRecyclerAdapter.onThumbnailClicked = { position ->
            imageViewPager.setCurrentItem(position, false)
            setThumbnailAsSelected(position)
        }

        setThumbnailAsSelected(0)
    }

    private fun setupToolbar() {
        parentActivity.setSupportActionBar(toolbar)
        parentActivity.supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowTitleEnabled(false)
        }

        toolbar.setNavigationOnClickListener {
            onBackNavigationPressed?.invoke(galleryData.indexOf(galleryData.first { it.selected }))
        }
    }
    // endregion

    // region Show/Hide System UI
    private fun setSystemUi(hide: Boolean) {
        GraniteActivityUtils.setSystemUiForActivity(parentActivity, hide)

        ViewCompat
            .animate(toolbar)
            .alpha(if (hide) 0f else 1f)
            .setDuration(250L)
            .start()

        ViewCompat
            .animate(description)
            .alpha(if (hide) 0f else 1f)
            .setDuration(250L)
            .start()

        ViewCompat
            .animate(counter)
            .alpha(if (hide) 0f else 1f)
            .setDuration(250L)
            .start()

        ViewCompat
            .animate(thumbnailRecyclerContainer)
            .translationY(if (hide) thumbnailRecyclerContainer.height.toFloat() else 0f)
            .setDuration(250L)
            .start()
    }
    // endregion

    // region Thumbnail Selection
    private fun setThumbnailAsSelected(position: Int) {
        if (position <= galleryData.size && position >= 0) {
            updateCounter(position)
            updateDescription(position)

            galleryData.find { it.selected }?.selected = false

            galleryData[position].selected = true
            thumbnailRecyclerAdapter.notifyDataSetChanged()
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

        description.visibility =  if (text.isNullOrBlank()) View.INVISIBLE else View.VISIBLE
        description.text = text
    }
    // endregion

    // region IO Events
    fun setBackPressedEvent(backPressedEvent: ((position: Int) -> Unit)) {
        onBackNavigationPressed = backPressedEvent
    }
    // endregion

}