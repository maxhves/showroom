package no.mhl.showroom.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import no.mhl.showroom.R
import no.mhl.showroom.model.GalleryImage
import no.mhl.showroom.ui.adapter.viewholder.ImagePagerViewHolder

class ImagePagerAdapter(data: List<GalleryImage>) :
    RecyclerView.Adapter<ImagePagerViewHolder>() {

    // region
    private val galleryData: List<GalleryImage> = listOf(data.last()) + data + listOf(data.first())
    // endregion

    // region Properties
    var onImageClicked: (() -> Unit)? = null
    // endregion

    // region Initialisation
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ImagePagerViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.row_image_pager,
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: ImagePagerViewHolder, position: Int) {
        holder.bind(galleryData[position], onImageClicked)
    }
    // endregion

    // region Accessors
    override fun getItemCount() = galleryData.size

    override fun getItemViewType(position: Int) = position
    // endregion

}