package no.mhl.showroom.data

import android.content.Context
import coil.Coil
import coil.api.load
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import no.mhl.showroom.data.model.GalleryData

/**
 * Suspended function that preloads upcoming images for a given data set and limit via [Coil.load]
 * @param context the context
 * @param position the current position of the data set
 * @param data the dataset to be used
 * @param preloadLimit the limit of how many upcoming images will be preloaded
 */
suspend fun preloadUpcomingImages(
    context: Context,
    position: Int,
    data: List<GalleryData>,
    preloadLimit: Int
) = withContext(Dispatchers.IO) {
    fun preload(item: GalleryData)  {
        Coil.load(context, item.image)
        Coil.load(context, item.downscaledImage)
    }

    if (position == 0) {
        for (i in 0..preloadLimit) {
            val currentPosition: Int = position + i
            if (currentPosition <= data.lastIndex) { preload(data[position]) }
        }
    } else {
        val nextPosition: Int = position + 1
        if (nextPosition <= data.lastIndex) { preload(data[position]) }
    }
}