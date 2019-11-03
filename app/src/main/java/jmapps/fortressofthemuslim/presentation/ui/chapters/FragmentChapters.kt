package jmapps.fortressofthemuslim.presentation.ui.chapters

import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import jmapps.fortressofthemuslim.R
import jmapps.fortressofthemuslim.data.database.DatabaseLists
import jmapps.fortressofthemuslim.data.database.DatabaseOpenHelper
import kotlinx.android.synthetic.main.fragment_chapters.view.*

class FragmentChapters : Fragment() {

    private lateinit var rootChapters: View

    private lateinit var database: SQLiteDatabase
    private lateinit var chapterList: MutableList<ModelChapters>
    private lateinit var adapterChapters: AdapterChapters

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        rootChapters = inflater.inflate(R.layout.fragment_chapters, container, false)

        database = DatabaseOpenHelper(context).readableDatabase
        chapterList = DatabaseLists(context).getChapterList

        val verticalLayout = LinearLayoutManager(context)
        rootChapters.rvMainChapters.layoutManager = verticalLayout

        adapterChapters = AdapterChapters(chapterList)
        rootChapters.rvMainChapters.adapter = adapterChapters

        return rootChapters
    }
}