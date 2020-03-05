package no.mhl.showroom.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import no.mhl.showroom.R
import no.mhl.showroom.data.model.GalleryData

class ThumbnailRecyclerAdapter(private val galleryData: List<GalleryData>) :
    RecyclerView.Adapter<ThumbnailRecyclerAdapter.ThumbnailViewHolder>() {

    var onThumbnailClicked: ((position: Int) -> Unit)? = null

    // region View Holder
    class ThumbnailViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView by lazy { view.findViewById<ImageView>(R.id.row_thumbnail_gallery_image) }
        val borderView: ImageView by lazy { view.findViewById<ImageView>(R.id.row_thumbnail_gallery_border) }
    }
    // endregion

    // region Initialisation
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ThumbnailViewHolder {
        return ThumbnailViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.row_thumbnail_gallery,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ThumbnailViewHolder, position: Int) {
        val item = galleryData[position]

        holder.borderView.visibility = when (item.selected) {
            true -> View.VISIBLE
            false -> View.GONE
        }

        holder.imageView.load(item.downscaledImage ?: item.image) {
            error(R.drawable.thumbnail_placeholder)
            placeholder(R.drawable.thumbnail_placeholder)
        }

        holder.itemView.setOnClickListener { onThumbnailClicked?.invoke(position) }
    }
    // endregion

    // region Accessors
    override fun getItemCount() = galleryData.size

    override fun getItemViewType(position: Int) = position
    // endregion

}