package no.mhl.showroom

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import no.mhl.showroom.data.model.GalleryData
import no.mhl.showroom.ui.Showroom

class MainActivity : AppCompatActivity() {

    // region Initialisation
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
    // endregion

}