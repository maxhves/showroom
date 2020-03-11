package no.mhl.showroom.ui.adapter

import android.view.*
import androidx.recyclerview.widget.RecyclerView
import no.mhl.showroom.R
import no.mhl.showroom.data.model.GalleryData
import no.mhl.showroom.ui.adapter.viewholder.ImagePagerViewHolder

class ImagePagerAdapter(private val galleryData: List<GalleryData>) :
    RecyclerView.Adapter<ImagePagerViewHolder>() {

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