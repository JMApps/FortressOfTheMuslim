package jmapps.fortressofthemuslim.presentation.ui.favoriteChapters

import android.app.SearchManager
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.text.TextUtils
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import jmapps.fortressofthemuslim.R
import jmapps.fortressofthemuslim.data.database.DatabaseLists
import jmapps.fortressofthemuslim.data.database.DatabaseOpenHelper
import kotlinx.android.synthetic.main.fragment_favorite_chapters.view.*

class FragmentFavoriteChapters : Fragment(), AdapterFavoriteChapters.OnItemClick,
    SearchView.OnQueryTextListener {

    private lateinit var rootFavoriteChapters: View

    private lateinit var database: SQLiteDatabase
    private lateinit var favoriteChapterList: MutableList<ModelFavoriteChapters>
    private lateinit var adapterFavoriteChapters: AdapterFavoriteChapters

    private var searchByFavoriteChapter: SearchView? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        rootFavoriteChapters = inflater.inflate(R.layout.fragment_favorite_chapters, container,false)

        database = DatabaseOpenHelper(context).readableDatabase
        favoriteChapterList = DatabaseLists(context).getFavoriteChapterList

        val verticalLayout = LinearLayoutManager(context)
        rootFavoriteChapters.rvMainFavorites.layoutManager = verticalLayout

        adapterFavoriteChapters = AdapterFavoriteChapters(favoriteChapterList, this)
        rootFavoriteChapters.rvMainFavorites.adapter = adapterFavoriteChapters

        if (adapterFavoriteChapters.itemCount <= 0) {
            rootFavoriteChapters.tvIsFavoriteListEmpty.visibility = View.VISIBLE
            rootFavoriteChapters.rvMainFavorites.visibility = View.GONE
            setHasOptionsMenu(false)
        } else {
            rootFavoriteChapters.tvIsFavoriteListEmpty.visibility = View.GONE
            rootFavoriteChapters.rvMainFavorites.visibility = View.VISIBLE
            setHasOptionsMenu(true)
        }

        return rootFavoriteChapters
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_favorite_chapters, menu)
        val searchManager = context?.getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchByFavoriteChapter = menu.findItem(R.id.action_search_favorite_chapter).actionView as SearchView?
        searchByFavoriteChapter?.setSearchableInfo(searchManager.getSearchableInfo(activity?.componentName))
        searchByFavoriteChapter?.maxWidth = Integer.MAX_VALUE
        searchByFavoriteChapter?.setOnQueryTextListener(this)
        return super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        if (TextUtils.isEmpty(newText)) {
            adapterFavoriteChapters.filter.filter("")
        } else {
            adapterFavoriteChapters.filter.filter(newText)
        }
        return true
    }

    override fun onItemClick(favoriteChapterId: Int) {
        setToast("Click = $favoriteChapterId")
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