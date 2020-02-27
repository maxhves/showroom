package no.mhl.showroom.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.alexvasilkov.gestures.views.GestureImageView
import no.mhl.showroom.R
import no.mhl.showroom.data.model.GalleryData

class ImagePagerAdapter(private val galleryData: List<GalleryData>) :
    RecyclerView.Adapter<ImagePagerAdapter.ImagePageViewHolder>() {

    var onImageClicked: (() -> Unit)? = null
    private var lastPosition: Int = 0

    // region View Holder
    class ImagePageViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val imageView: GestureImageView by lazy { view.findViewById<GestureImageView>(R.id.row_image_gallery_image) }
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

    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: ImagePageViewHolder, position: Int) {
        val item = galleryData[position]

        holder.imageView.load(item.image)

        holder.imageView.setOnClickListener { onImageClicked?.invoke() }

        lastPosition = position
    }
    // endregion

    // region Accessors
    override fun getItemCount() = galleryData.size

    override fun getItemViewType(position: Int) = position
    // endregion

    override fun onViewDetachedFromWindow(holder: ImagePageViewHolder) {
        super.onViewDetachedFromWindow(holder)
        holder.itemView.clearAnimation()
    }

}