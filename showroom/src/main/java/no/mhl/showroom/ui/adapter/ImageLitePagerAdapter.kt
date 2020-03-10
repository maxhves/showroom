package no.mhl.showroom.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import no.mhl.showroom.R
import no.mhl.showroom.data.model.GalleryData

class ImageLitePagerAdapter(private val data: List<GalleryData>) :
    RecyclerView.Adapter<ImageLitePagerAdapter.ImageLiteViewHolder>() {

    // region Click Properties
    var onImageClicked: (() -> Unit)? = null
    // endregion

    // region View Holder
    class ImageLiteViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView by lazy { view.findViewById<ImageView>(R.id.image) }
    }
    // endregion

    // region Initialisation
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageLiteViewHolder {
        return ImageLiteViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.row_image_pager_lite,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ImageLiteViewHolder, position: Int) {
        val item = data[position]

        holder.imageView.load(item.image) { error(R.drawable.ic_error) }

        holder.imageView.setOnClickListener { onImageClicked?.invoke() }
    }
    // endregion

    // region Accessors
    override fun getItemCount() = data.size

    override fun getItemViewType(position: Int) = position
    // endregion

}