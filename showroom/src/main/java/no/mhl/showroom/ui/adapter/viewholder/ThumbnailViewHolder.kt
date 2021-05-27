package no.mhl.showroom.ui.adapter.viewholder

import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import no.mhl.showroom.R
import no.mhl.showroom.model.GalleryImage

class ThumbnailViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

    // region Properties
    private val imageView: ImageView by lazy { view.findViewById<ImageView>(R.id.row_thumbnail_gallery_image) }
    private val frameView: ImageView by lazy { view.findViewById<ImageView>(R.id.row_thumbnail_gallery_border) }
    // endregion

    // region Binding
    fun bind(item: GalleryImage, thumbnailClickedEvent: ((position: Int) -> Unit)?, position: Int) {
        frameView.visibility = when (item.selected) {
            true -> View.VISIBLE
            false -> View.GONE
        }

        imageView.load(item.downscaledUrl) {
            error(R.drawable.thumbnail_placeholder)
            placeholder(R.drawable.thumbnail_placeholder)
            crossfade(true)
        }

        itemView.setOnClickListener { thumbnailClickedEvent?.invoke(position) }
    }
    // endregion

}