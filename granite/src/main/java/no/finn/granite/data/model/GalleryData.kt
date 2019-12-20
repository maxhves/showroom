package no.finn.granite.data.model

data class GalleryData (
    val description: String? = null,
    val image: String,
    var selected: Boolean = false,
    var downscaledImage: String? = null
)