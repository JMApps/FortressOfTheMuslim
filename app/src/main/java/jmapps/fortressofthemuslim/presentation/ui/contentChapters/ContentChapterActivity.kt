package jmapps.fortressofthemuslim.presentation.ui.contentChapters

import android.annotation.SuppressLint
import android.content.*
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.text.Html
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import jmapps.fortressofthemuslim.R
import jmapps.fortressofthemuslim.data.database.DatabaseContents
import jmapps.fortressofthemuslim.data.database.DatabaseLists
import jmapps.fortressofthemuslim.data.database.DatabaseOpenHelper
import jmapps.fortressofthemuslim.presentation.mvp.favoriteSupplications.ContractFavoriteSupplications
import jmapps.fortressofthemuslim.presentation.mvp.favoriteSupplications.FavoriteSupplicationPresenterImpl
import jmapps.fortressofthemuslim.presentation.ui.chapters.ModelChapters
import kotlinx.android.synthetic.main.activity_content_chapter.*
import kotlinx.android.synthetic.main.content_chapter.*

class ContentChapterActivity : AppCompatActivity(), AdapterChapterContents.AddRemoveFavorite,
    AdapterChapterContents.ItemShare, AdapterChapterContents.ItemCopy,
    ContractFavoriteSupplications.ViewFavoriteSupplications {

    private var chapterId: Int? = null

    private lateinit var preferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    private lateinit var chapterListName: MutableList<ModelChapters>

    private lateinit var database: SQLiteDatabase
    private lateinit var chapterContentList: MutableList<ModelChapterContents>
    private lateinit var adapterChapterContents: AdapterChapterContents

    private lateinit var favoriteSupplicationPresenterImpl: FavoriteSupplicationPresenterImpl

    private var clipboard: ClipboardManager? = null
    private var clip: ClipData? = null

    @SuppressLint("CommitPrefEdits")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_content_chapter)
        setSupportActionBar(toolbar)

        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)

        database = DatabaseOpenHelper(this).readableDatabase

        preferences = PreferenceManager.getDefaultSharedPreferences(this)
        editor = preferences.edit()

        chapterId = intent.getIntExtra("key_chapter_id", 1)
        chapterListName = DatabaseLists(this).getChapterName
        actionBar?.title = getString(R.string.title_activity_content_chapter) + " $chapterId"
        tvContentChapterTitle.text = Html.fromHtml(chapterListName[chapterId!! - 1].strChapterTitle)

        val verticalLayout = LinearLayoutManager(this)
        rvChapterContent.layoutManager = verticalLayout

        chapterContentList = DatabaseContents(this).getChapterContentList(chapterId)
        adapterChapterContents = AdapterChapterContents(
            chapterContentList, this, preferences, this, this)
        rvChapterContent.adapter = adapterChapterContents

        favoriteSupplicationPresenterImpl = FavoriteSupplicationPresenterImpl(this, database)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun copy(content: String) {
        clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager?
        clip = ClipData.newPlainText("", Html.fromHtml(content))
        clipboard?.setPrimaryClip(clip!!)
        setToast(getString(R.string.copied_to_clipboard))
    }

    override fun share(content: String) {
        val shareLink = Intent(Intent.ACTION_SEND)
        shareLink.type = "text/plain"
        shareLink.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(content))
        startActivity(shareLink)
    }

    override fun addRemove(state: Boolean, chapterContentId: Int) {
        favoriteSupplicationPresenterImpl.addRemoveFavorite(state, chapterContentId)
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

    override fun saveCurrentFavoriteItem(keyFavoriteSupplication: String, stateFavoriteSupplication: Boolean) {
        editor.putBoolean(keyFavoriteSupplication, stateFavoriteSupplication).apply()
    }

    private fun setToast(message: String) {
        val toast = Toast.makeText(this, message, Toast.LENGTH_LONG)
        val view: View = toast.view
        view.setBackgroundResource(R.drawable.circle_toast_background)
        val text = view.findViewById(android.R.id.message) as TextView
        text.setPadding(32, 16, 32, 16)
        text.setTextColor(resources.getColor(R.color.white))
        toast.show()
    }
}