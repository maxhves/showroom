package no.mhl.showroom.ui.adapter.viewholder

import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import no.mhl.showroom.R
import no.mhl.showroom.model.GalleryImage

class ImageLiteViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

    // region Properties
    private val imageView: ImageView by lazy { view.findViewById<ImageView>(R.id.image) }
    // endregion

    // region Binding
    fun bind(item: GalleryImage, imageClickedEvent: (() -> Unit)?) {
        imageView.load(item.url) { error(R.drawable.ic_error) }
        imageView.setOnClickListener { imageClickedEvent?.invoke() }
    }
    // endregion

}