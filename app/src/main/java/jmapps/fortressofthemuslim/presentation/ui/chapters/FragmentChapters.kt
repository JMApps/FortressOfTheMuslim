package jmapps.fortressofthemuslim.presentation.ui.chapters

import android.app.SearchManager
import android.content.Context.SEARCH_SERVICE
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.text.TextUtils
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import jmapps.fortressofthemuslim.R
import jmapps.fortressofthemuslim.data.database.DatabaseLists
import jmapps.fortressofthemuslim.data.database.DatabaseOpenHelper
import kotlinx.android.synthetic.main.fragment_chapters.view.*

class FragmentChapters : Fragment(), SearchView.OnQueryTextListener, AdapterChapters.OnItemClick {

    private lateinit var rootChapters: View

    private lateinit var database: SQLiteDatabase
    private lateinit var chapterList: MutableList<ModelChapters>
    private lateinit var adapterChapters: AdapterChapters

    private var searchView: SearchView? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        rootChapters = inflater.inflate(R.layout.fragment_chapters, container, false)

        setHasOptionsMenu(true)

        database = DatabaseOpenHelper(context).readableDatabase
        chapterList = DatabaseLists(context).getChapterList

        val verticalLayout = LinearLayoutManager(context)
        rootChapters.rvMainChapters.layoutManager = verticalLayout

        adapterChapters = AdapterChapters(chapterList, this)
        rootChapters.rvMainChapters.adapter = adapterChapters

        return rootChapters
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_chapters, menu)
        val searchManager = context?.getSystemService(SEARCH_SERVICE) as SearchManager
        searchView = menu.findItem(R.id.action_search_chapter).actionView as SearchView?
        searchView?.setSearchableInfo(searchManager.getSearchableInfo(activity?.componentName))
        searchView?.maxWidth = Integer.MAX_VALUE
        searchView?.setOnQueryTextListener(this)
        return super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        if (TextUtils.isEmpty(newText)) {
            adapterChapters.filter.filter("")
        } else {
            adapterChapters.filter.filter(newText)
        }
        return true
    }

    override fun onItemClick(chapterId: Int) {
        setToast("Click = $chapterId")
    }

    private fun setToast(message: String) {
        val toast = Toast.makeText(context, message, Toast.LENGTH_LONG)
        val view: View = toast.view
        view.setBackgroundResource(R.drawable.circle_toast_background)
        val text = view.findViewById(android.R.id.message) as TextView
        text.setPadding(32, 16, 32, 16)
        text.setTextColor(resources.getColor(R.color.white))
        toast.show()
    }
}