package jmapps.fortressofthemuslim.presentation.ui.favoriteChapters

import android.annotation.SuppressLint
import android.app.SearchManager
import android.content.Context
import android.content.SharedPreferences
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.text.TextUtils
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import jmapps.fortressofthemuslim.R
import jmapps.fortressofthemuslim.data.database.DatabaseLists
import jmapps.fortressofthemuslim.data.database.DatabaseOpenHelper
import jmapps.fortressofthemuslim.presentation.mvp.favoriteChapters.ContractFavoriteChapters
import jmapps.fortressofthemuslim.presentation.mvp.favoriteChapters.FavoriteChapterPresenterImpl
import kotlinx.android.synthetic.main.fragment_favorite_chapters.view.*

class FragmentFavoriteChapters : Fragment(), AdapterFavoriteChapters.OnItemClick,
    SearchView.OnQueryTextListener, AdapterFavoriteChapters.AddRemoveFavorite,
    ContractFavoriteChapters.ViewFavoriteChapters {

    private lateinit var rootFavoriteChapters: View

    private lateinit var preferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    private lateinit var database: SQLiteDatabase
    private lateinit var favoriteChapterList: MutableList<ModelFavoriteChapters>
    private lateinit var adapterFavoriteChapters: AdapterFavoriteChapters

    private lateinit var favoriteChapterPresenterImpl: FavoriteChapterPresenterImpl

    private var searchByFavoriteChapter: SearchView? = null

    @SuppressLint("CommitPrefEdits")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        rootFavoriteChapters = inflater.inflate(R.layout.fragment_favorite_chapters, container,false)

        preferences = PreferenceManager.getDefaultSharedPreferences(context)
        editor = preferences.edit()

        database = DatabaseOpenHelper(context).readableDatabase
        favoriteChapterList = DatabaseLists(context).getFavoriteChapterList

        val verticalLayout = LinearLayoutManager(context)
        rootFavoriteChapters.rvFavoriteChapters.layoutManager = verticalLayout

        adapterFavoriteChapters = AdapterFavoriteChapters(
            favoriteChapterList, this, preferences, this)
        rootFavoriteChapters.rvFavoriteChapters.adapter = adapterFavoriteChapters

        if (adapterFavoriteChapters.itemCount <= 0) {
            rootFavoriteChapters.tvIsFavoriteListEmpty.visibility = View.VISIBLE
            rootFavoriteChapters.rvFavoriteChapters.visibility = View.GONE
            setHasOptionsMenu(false)
        } else {
            rootFavoriteChapters.tvIsFavoriteListEmpty.visibility = View.GONE
            rootFavoriteChapters.rvFavoriteChapters.visibility = View.VISIBLE
            setHasOptionsMenu(true)
        }

        favoriteChapterPresenterImpl = FavoriteChapterPresenterImpl(this, database)

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

    override fun addRemove(state: Boolean, favoriteChapterId: Int) {
        favoriteChapterPresenterImpl.addRemoveFavorite(state, favoriteChapterId)
    }

    override fun showFavoriteStateToast(state: Boolean) {
        if (state) {
            setToast(getString(R.string.favorite_add))
        } else {
            setToast(getString(R.string.favorite_removed))
        }
    }

    override fun showDBExceptionToast(error: String) {
        setToast(getString(R.string.database_exception) + error)
    }

    override fun saveCurrentFavoriteItem(keyFavoriteChapter: String, stateFavoriteChapter: Boolean) {
        editor.putBoolean(keyFavoriteChapter, stateFavoriteChapter).apply()
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