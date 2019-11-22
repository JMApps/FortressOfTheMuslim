package jmapps.fortressofthemuslim.presentation.ui.favoriteChapters

import android.annotation.SuppressLint
import android.app.SearchManager
import android.content.Context
import android.content.Intent
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
import jmapps.fortressofthemuslim.presentation.mvp.main.MainContract
import jmapps.fortressofthemuslim.presentation.mvp.main.MainPresenterImpl
import jmapps.fortressofthemuslim.presentation.ui.contentChapters.ContentChapterActivity
import kotlinx.android.synthetic.main.fragment_favorite_chapters.view.*

class FragmentFavoriteChapters : Fragment(), AdapterFavoriteChapters.OnItemClick,
    SearchView.OnQueryTextListener, AdapterFavoriteChapters.AddRemoveFavoriteChapter,
    ContractFavoriteChapters.ViewFavoriteChapters, MainContract.MainView {

    private lateinit var rootFavoriteChapters: View

    private lateinit var preferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    private lateinit var database: SQLiteDatabase
    private lateinit var favoriteChapterList: MutableList<ModelFavoriteChapters>
    private lateinit var adapterFavoriteChapters: AdapterFavoriteChapters

    private lateinit var mainPresenterImpl: MainPresenterImpl
    private lateinit var favoriteChapterPresenterImpl: FavoriteChapterPresenterImpl

    private var searchByFavoriteChapter: SearchView? = null

    @SuppressLint("CommitPrefEdits")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        rootFavoriteChapters = inflater.inflate(R.layout.fragment_favorite_chapters, container, false)

        preferences = PreferenceManager.getDefaultSharedPreferences(context)
        editor = preferences.edit()

        database = DatabaseOpenHelper(context).readableDatabase
        favoriteChapterList = DatabaseLists(context).getFavoriteChapterList

        val verticalLayout = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
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

        mainPresenterImpl = MainPresenterImpl(this, context)
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

    override fun addRemoveChapter(state: Boolean, favoriteChapterId: Int) {
        favoriteChapterPresenterImpl.addRemoveFavoriteChapter(state, favoriteChapterId)
    }

    override fun showFavoriteChapterStateToast(state: Boolean) {
        if (state) {
            mainPresenterImpl.setToastMessage(getString(R.string.favorite_chapter_add))
        } else {
            mainPresenterImpl.setToastMessage(getString(R.string.favorite_chapter_removed))
        }
    }

    override fun showDBExceptionChapterToast(error: String) {
        mainPresenterImpl.setToastMessage(getString(R.string.database_exception) + error)
    }

    override fun saveCurrentFavoriteChapterItem(keyFavoriteChapter: String, stateFavoriteChapter: Boolean) {
        editor.putBoolean(keyFavoriteChapter, stateFavoriteChapter).apply()
    }

    override fun onItemClick(favoriteChapterId: Int) {
        val toContentChapterActivity = Intent(context, ContentChapterActivity::class.java)
        toContentChapterActivity.putExtra("key_chapter_id", favoriteChapterId)
        context?.startActivity(toContentChapterActivity)
    }
}