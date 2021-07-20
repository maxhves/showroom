package no.mhl.showroom.ui

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.viewpager2.widget.ViewPager2
import coil.Coil
import coil.ImageLoader
import coil.util.CoilUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import no.mhl.showroom.R
import no.mhl.showroom.data.preloadUpcomingImages
import no.mhl.showroom.model.GalleryImage
import no.mhl.showroom.ui.adapter.ImageLitePagerAdapter
import no.mhl.showroom.util.setCount
import no.mhl.showroom.util.setDescription
import okhttp3.OkHttpClient

class ShowroomLite(
    context: Context,
    attrs: AttributeSet?
) : FrameLayout(context, attrs) {

    // region View Properties
    private val viewPager by lazy { findViewById<ViewPager2>(R.id.view_pager) }
    private val descriptionText by lazy { findViewById<TextView>(R.id.description) }
    private val countText by lazy { findViewById<TextView>(R.id.image_counter) }
    // endregion

    // region Properties
    private lateinit var galleryData: List<GalleryImage>
    private lateinit var imagePagerAdapter: ImageLitePagerAdapter
    private var initialPosition: Int = 0
    val currentPosition: Int
        get() = viewPager.currentItem
    // endregion

    // region Click Exposure Properties
    var onImageClicked: (() -> Unit)? = null
    // endregion

    // region Custom Attribute Properties
    private var fontPrimary: Typeface? = Typeface.DEFAULT
    private var fontSecondary: Typeface? = Typeface.DEFAULT
    private var imagePreloadLimit: Int = 3
    // endregion

    // region Initialisation
    init {
        LayoutInflater.from(context).inflate(R.layout.layout_lite_showroom, this)

        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.ShowroomLite,
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
            val fontSecondaryRes = typedArray.getResourceId(R.styleable.Showroom_fontFamilySecondary, 0)
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
        } finally { typedArray.recycle() }
    }

    fun setupWithData(data: List<GalleryImage>, startAtIndex: Int = 0) {
        galleryData = data
        initialPosition = startAtIndex
        setupViews()
    }

    private fun setupViews() {
        setupViewPager()
    }

    private fun setupViewPager() {
        imagePagerAdapter = ImageLitePagerAdapter(galleryData)
        viewPager.apply {
            adapter = imagePagerAdapter
            offscreenPageLimit = imagePreloadLimit
        }

        imagePagerAdapter.onImageClicked = { onImageClicked?.invoke() }

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                setupItemDetails(position)
                preload(position)
            }
        })

        setupItemDetails(0)
    }

    private fun setupItemDetails(position: Int) {
        countText.setCount(position, galleryData.size)
        descriptionText.setDescription(galleryData[position].description)
    }
    // endregion

    // region Preloading
    private fun preload(position: Int) = CoroutineScope(Dispatchers.IO).launch {
        preloadUpcomingImages(context, position, galleryData, imagePreloadLimit)
    }
    // endregion

}