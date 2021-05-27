package no.mhl.showroom.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import no.mhl.showroom.R
import no.mhl.showroom.model.GalleryImage
import no.mhl.showroom.ui.adapter.viewholder.ThumbnailViewHolder

class ThumbnailRecyclerAdapter(private val galleryData: List<GalleryImage>) :
    RecyclerView.Adapter<ThumbnailViewHolder>() {

    // region Click Exposure
    var onThumbnailClicked: ((position: Int) -> Unit)? = null
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
        holder.bind(galleryData[position], onThumbnailClicked, position)
    }
    // endregion

    // region Accessors
    override fun getItemCount() = galleryData.size

    override fun getItemViewType(position: Int) = position
    // endregion

}