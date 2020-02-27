package no.finn.showroom.data.model

data class GalleryData (
    val description: String? = null,
    val image: String,
    var selected: Boolean = false,
    var downscaledImage: String? = null
)