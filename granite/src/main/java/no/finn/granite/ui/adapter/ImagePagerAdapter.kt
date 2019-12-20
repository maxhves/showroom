package no.finn.granite.ui.adapter

import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat.canScrollHorizontally
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.ortiz.touchview.TouchImageView
import no.finn.granite.R
import no.finn.granite.data.model.GalleryData

class ImagePagerAdapter(private val galleryData: List<GalleryData>) :
    RecyclerView.Adapter<ImagePagerAdapter.ImagePageViewHolder>() {

    var onImageClicked: (() -> Unit)? = null
    private var lastPosition: Int = 0

    // region View Holder
    class ImagePageViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val imageView: TouchImageView by lazy { view.findViewById<TouchImageView>(R.id.row_image_gallery_image) }
    }
    // endregion

    // region Initialisation
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImagePageViewHolder {
        return ImagePageViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.row_image_pager,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ImagePageViewHolder, position: Int) {
        holder.setIsRecyclable(false)

        val item = galleryData[position]

        holder.imageView.load(item.image) { crossfade(true) }

        holder.imageView.setOnClickListener { onImageClicked?.invoke() }

        lastPosition = position

        holder.imageView.setOnTouchListener { view, event ->
            var result = true

            if (event.pointerCount >= 2 || view.canScrollHorizontally(1) && canScrollHorizontally(view, -1)) {
                result = when (event.action) {
                    MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> {
                        holder.imageView.parent.requestDisallowInterceptTouchEvent(true)
                        false
                    }
                    MotionEvent.ACTION_UP -> {
                        holder.imageView.parent.requestDisallowInterceptTouchEvent(false)
                        true
                    }
                    else -> true
                }
            }
            result
        }

    }
    // endregion

    // region Accessors
    override fun getItemCount() = galleryData.size
    // endregion

    override fun onViewDetachedFromWindow(holder: ImagePageViewHolder) {
        super.onViewDetachedFromWindow(holder)
        holder.itemView.clearAnimation()
    }

}