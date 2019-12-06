package no.finn.granite.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import no.finn.granite.R
import no.finn.granite.data.model.GalleryData

class ThumbnailRecyclerAdapter(private val galleryData: List<GalleryData>) :
    RecyclerView.Adapter<ThumbnailRecyclerAdapter.ThumbnailViewHolder>() {

    var onThumbnailClicked: ((position: Int) -> Unit)? = null
    // region Properties
//    private val onImageClickedSubject: PublishSubject<Int> = PublishSubject.create()
    // endregion

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
        holder.setIsRecyclable(false)

        val item = galleryData[position]

        if (item.selected) { holder.borderView.visibility = View.VISIBLE }

        holder.imageView.load(item.image)

        holder.itemView.setOnClickListener { onThumbnailClicked?.invoke(position) }

        //Glide.with(holder.itemView.context).load(item.image).into(holder.imageView)

        //holder.itemView.setOnClickListener { onImageClickedSubject.onNext(position) }

        //if ((position + 1) == galleryData.size && item.selected) { holder.lastItemView.visibility = View.VISIBLE }
    }
    // endregion

    // region Detachment
//    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
//        super.onDetachedFromRecyclerView(recyclerView)
//    }
    // endregion

    // region Accessors
    override fun getItemCount() = galleryData.size

//    fun imageOnClicked() = onImageClickedSubject
    // endregion

}