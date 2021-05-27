package no.mhl.showroom.model

data class GalleryImage (
    val description: String? = null,
    val url: String,
    var selected: Boolean = false,
    var downscaledUrl: String? = null
)