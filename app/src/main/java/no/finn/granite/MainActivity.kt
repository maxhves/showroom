package no.finn.granite

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import no.finn.granite.data.model.GalleryData
import no.finn.granite.ui.GraniteImageGallery

class MainActivity : AppCompatActivity() {

    // region Properties
    private val imageGallery: GraniteImageGallery by lazy { findViewById<GraniteImageGallery>(R.id.gallery_image) }
    private val testGalleryData: List<GalleryData> = listOf(
        GalleryData(
            "One",
            "https://images.finncdn.no/dynamic/1280w/2019/12/vertical-2/03/8/164/776/798_624463418.jpg"
        ),
        GalleryData(
            "Two",
            "https://images.finncdn.no/dynamic/1280w/2019/12/vertical-2/03/8/164/776/798_108199866.jpg"
        ),
        GalleryData(
            "Three",
            "https://images.finncdn.no/dynamic/1280w/2019/12/vertical-2/03/8/164/776/798_1334522055.jpg"
        ),
        GalleryData(
            "Four",
            "https://images.finncdn.no/dynamic/960w/2019/12/vertical-2/03/8/164/776/798_1159721068.jpg"
        ),
        GalleryData(
            "Five",
            "https://images.finncdn.no/dynamic/1280w/2019/12/vertical-2/03/8/164/776/798_793159008.jpg"
        ),
        GalleryData(
            "Six",
            "https://images.finncdn.no/dynamic/1280w/2019/12/vertical-2/03/8/164/776/798_1944119106.jpg"
        ),
        GalleryData(
            "Seven",
            "https://images.finncdn.no/dynamic/1280w/2019/12/vertical-2/03/8/164/776/798_1710984361.jpg"
        ),
        GalleryData(
            "Eight",
            "https://images.finncdn.no/dynamic/1280w/2019/12/vertical-2/03/8/164/776/798_1285217046.jpg"
        ),
        GalleryData(
            "Nine",
            "https://images.finncdn.no/dynamic/1600w/2019/12/vertical-2/03/8/164/776/798_1905306971.jpg"
        ),
        GalleryData(
            "",
            "https://images.finncdn.no/dynamic/1280w/2019/12/vertical-2/03/8/164/776/798_2033072057.jpg"
        )
    )
    // endregion

    // region Initialisation
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupView()
    }
    // endregion

    // region View Setup
    private fun setupView() {
        imageGallery.attachToActivity(this, testGalleryData)
        imageGallery.setBackPressedEvent {
            finish()
        }
    }
    // endregion

}
