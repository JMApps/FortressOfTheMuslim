package jmapps.fortressofthemuslim.presentation.ui.chapters

import android.annotation.SuppressLint
import android.app.SearchManager
import android.content.Context.SEARCH_SERVICE
import android.content.Intent
import android.content.SharedPreferences
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.text.TextUtils
import android.view.*
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
import kotlinx.android.synthetic.main.fragment_chapters.view.*

class FragmentChapters : Fragment(), SearchView.OnQueryTextListener, AdapterChapters.OnItemClick,
    AdapterChapters.AddRemoveFavoriteChapter, ContractFavoriteChapters.ViewFavoriteChapters,
    MainContract.MainView {

    private lateinit var rootChapters: View

    private lateinit var preferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    private lateinit var database: SQLiteDatabase
    private lateinit var chapterList: MutableList<ModelChapters>
    private lateinit var adapterChapters: AdapterChapters

    private lateinit var mainPresenterImpl: MainPresenterImpl
    private lateinit var favoriteChapterPresenterImpl: FavoriteChapterPresenterImpl

    private var searchView: SearchView? = null

    @SuppressLint("CommitPrefEdits")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        rootChapters = inflater.inflate(R.layout.fragment_chapters, container, false)

        setHasOptionsMenu(true)

        preferences = PreferenceManager.getDefaultSharedPreferences(context)
        editor = preferences.edit()

        database = DatabaseOpenHelper(context).readableDatabase
        chapterList = DatabaseLists(context).getChapterList

        val verticalLayout = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rootChapters.rvMainChapters.layoutManager = verticalLayout

        adapterChapters = AdapterChapters(chapterList, this, preferences,this)
        rootChapters.rvMainChapters.adapter = adapterChapters

        mainPresenterImpl = MainPresenterImpl(this, context)
        favoriteChapterPresenterImpl = FavoriteChapterPresenterImpl(this, database)

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

    override fun addRemoveChapter(state: Boolean, chapterId: Int) {
        favoriteChapterPresenterImpl.addRemoveFavoriteChapter(state, chapterId)
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

    override fun onItemClick(chapterId: Int) {
        val toContentChapterActivity = Intent(context, ContentChapterActivity::class.java)
        toContentChapterActivity.putExtra("key_chapter_id", chapterId)
        context?.startActivity(toContentChapterActivity)
    }
}