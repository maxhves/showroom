package no.mhl.showroom.ui.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.alexvasilkov.gestures.views.GestureImageView
import no.mhl.showroom.R
import no.mhl.showroom.data.model.GalleryData

class ImagePagerViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

    // region Properties
    private val imageView: GestureImageView by lazy { view.findViewById<GestureImageView>(R.id.row_image_gallery_image) }
    // endregion

    // region Binding
    fun bind(item: GalleryData, imageClickedEvent: (() -> Unit)?) {
        imageView.load(item.image) { error(R.drawable.ic_error) }
        imageView.setOnClickListener { imageClickedEvent?.invoke() }
    }
    // endregion

}