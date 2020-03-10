package no.mhl.showroom.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import no.mhl.showroom.R
import no.mhl.showroom.data.provideGalleryData
import no.mhl.showroom.ui.ShowroomLite

class AdDetailFragment : Fragment() {

    // region View Properties
    private lateinit var showroomLite: ShowroomLite
    // endregion

    // region Initialisation
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_detail_ad, container,false)

        setupView(view)

        return view
    }
    // endregion

    // region View Setup
    private fun setupView(view: View) {
        showroomLite = view.findViewById(R.id.showroom_lite)

        setupShowroomLite()
    }

    private fun setupShowroomLite() {
        showroomLite.setupWithData(provideGalleryData())
        showroomLite.onImageClicked = ::openGalleryFragment
    }
    // endregion

    // region Misc
    private fun openGalleryFragment() {
        findNavController().navigate(R.id.action_adDetailFragment_to_galleryFragment)
    }
    // endregion

}