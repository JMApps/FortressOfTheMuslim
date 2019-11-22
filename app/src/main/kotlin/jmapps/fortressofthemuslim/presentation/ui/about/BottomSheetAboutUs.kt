package jmapps.fortressofthemuslim.presentation.ui.about

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import jmapps.fortressofthemuslim.R

class BottomSheetAboutUs: BottomSheetDialogFragment() {

    private lateinit var rootAboutUs: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        rootAboutUs = inflater.inflate(R.layout.bottom_sheet_about_us, container, false)

        return rootAboutUs
    }
}