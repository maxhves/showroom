package no.finn.granite.ui

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import no.finn.granite.R

class GraniteImageGallery
constructor(context: Context, attrs: AttributeSet?) : FrameLayout(context, attrs) {

    init {
        LayoutInflater.from(context).inflate(R.layout.layout_gallery_image_granite, this)
    }

}