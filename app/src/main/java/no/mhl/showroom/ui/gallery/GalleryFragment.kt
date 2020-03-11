package no.mhl.showroom.ui.gallery

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import no.mhl.showroom.R
import no.mhl.showroom.data.provideGalleryData
import no.mhl.showroom.ui.Showroom

class GalleryFragment : Fragment() {

    // region View Properties
    private lateinit var showroom: Showroom
    // endregion

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_gallery, container, false)

        setupView(view)

        return view
    }
    // endregion

    // region View Setup
    private fun setupView(view: View) {
        showroom = view.findViewById(R.id.showroom)

        setupShowroom()
    }

    private fun setupShowroom() {
        showroom.attach((activity as AppCompatActivity), provideGalleryData())
        showroom.setBackPressedEvent { findNavController().popBackStack() }
    }
    // endregion

}