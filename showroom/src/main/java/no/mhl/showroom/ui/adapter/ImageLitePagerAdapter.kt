package no.mhl.showroom.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import no.mhl.showroom.R
import no.mhl.showroom.model.GalleryImage
import no.mhl.showroom.ui.adapter.viewholder.ImageLiteViewHolder

class ImageLitePagerAdapter(private val data: List<GalleryImage>) :
    RecyclerView.Adapter<ImageLiteViewHolder>() {

    // region Click Properties
    var onImageClicked: (() -> Unit)? = null
    // endregion

    // region Initialisation
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ImageLiteViewHolder(
        LayoutInflater.from(parent.context).inflate(
            R.layout.row_image_pager_lite,
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: ImageLiteViewHolder, position: Int) {
        holder.bind(data[position], onImageClicked)
    }
    // endregion

    // region Accessors
    override fun getItemCount() = data.size

    override fun getItemViewType(position: Int) = position
    // endregion

}