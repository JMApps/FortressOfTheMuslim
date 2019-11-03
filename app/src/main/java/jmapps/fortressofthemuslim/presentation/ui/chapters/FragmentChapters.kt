package jmapps.fortressofthemuslim.presentation.ui.chapters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import jmapps.fortressofthemuslim.R

class FragmentChapters : Fragment() {

    private lateinit var rootChapters: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        rootChapters = inflater.inflate(R.layout.fragment_chapters, container, false)

        return rootChapters
    }
}