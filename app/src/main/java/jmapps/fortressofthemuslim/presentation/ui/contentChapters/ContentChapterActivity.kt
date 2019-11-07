package jmapps.fortressofthemuslim.presentation.ui.contentChapters

import android.annotation.SuppressLint
import android.content.*
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.text.Html
import android.view.Menu
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
import jmapps.fortressofthemuslim.presentation.mvp.favoriteChapters.ContractFavoriteChapters
import jmapps.fortressofthemuslim.presentation.mvp.favoriteChapters.FavoriteChapterPresenterImpl
import jmapps.fortressofthemuslim.presentation.mvp.favoriteSupplications.ContractFavoriteSupplications
import jmapps.fortressofthemuslim.presentation.mvp.favoriteSupplications.FavoriteSupplicationPresenterImpl
import jmapps.fortressofthemuslim.presentation.ui.chapters.ModelChapters
import kotlinx.android.synthetic.main.activity_content_chapter.*
import kotlinx.android.synthetic.main.content_chapter.*

class ContentChapterActivity : AppCompatActivity(), AdapterChapterContents.ItemShare,
    AdapterChapterContents.ItemCopy, ContractFavoriteSupplications.ViewFavoriteSupplications,
    AdapterChapterContents.AddRemoveFavoriteSupplication,
    ContractFavoriteChapters.ViewFavoriteChapters {

    private var chapterId: Int? = null

    private lateinit var preferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    private lateinit var chapterListName: MutableList<ModelChapters>

    private lateinit var database: SQLiteDatabase
    private lateinit var chapterContentList: MutableList<ModelChapterContents>
    private lateinit var adapterChapterContents: AdapterChapterContents

    private lateinit var favoriteChapterPresenterImpl: FavoriteChapterPresenterImpl
    private lateinit var favoriteSupplicationPresenterImpl: FavoriteSupplicationPresenterImpl

    private var clipboard: ClipboardManager? = null
    private var clip: ClipData? = null

    private lateinit var contentNumber: MenuItem

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

        favoriteChapterPresenterImpl = FavoriteChapterPresenterImpl(this, database)
        favoriteSupplicationPresenterImpl = FavoriteSupplicationPresenterImpl(this, database)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_content_chapters, menu)
        contentNumber = menu.findItem(R.id.action_content_number)
        val favoriteState = preferences.getBoolean("key_chapter_bookmark_$chapterId", false)
        if (favoriteState) {
            contentNumber.setIcon(R.drawable.ic_item_content_favorite_true)
        } else {
            contentNumber.setIcon(R.drawable.ic_item_content_favorite_false)
        }
        contentNumber.isChecked = favoriteState
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {

            R.id.action_content_number -> {
                if (!item.isChecked) {
                    contentNumber.isChecked = true
                    contentNumber.setIcon(R.drawable.ic_item_content_favorite_true)
                } else {
                    contentNumber.isChecked = false
                    contentNumber.setIcon(R.drawable.ic_item_content_favorite_false)
                }
                favoriteChapterPresenterImpl.addRemoveFavoriteChapter(contentNumber.isChecked, chapterId!!)
            }

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

    override fun showFavoriteChapterStateToast(state: Boolean) {
        if (state) {
            setToast(getString(R.string.content_favorite_add))
        } else {
            setToast(getString(R.string.content_favorite_removed))
        }
    }

    override fun showDBExceptionChapterToast(error: String) {
        setToast(getString(R.string.database_exception) + error)
    }

    override fun saveCurrentFavoriteChapterItem(keyFavoriteChapter: String, stateFavoriteChapter: Boolean) {
        editor.putBoolean(keyFavoriteChapter, stateFavoriteChapter).apply()
    }

    override fun addRemoveSupplication(state: Boolean, chapterContentId: Int) {
        favoriteSupplicationPresenterImpl.addRemoveFavoriteSupplication(state, chapterContentId)
    }

    override fun showFavoriteSupplicationStateToast(state: Boolean) {
        if (state) {
            setToast(getString(R.string.favorite_add))
        } else {
            setToast(getString(R.string.favorite_removed))
        }
    }

    override fun showDBExceptionSupplicationToast(error: String) {
        setToast(getString(R.string.database_exception) + error)
    }

    override fun saveCurrentFavoriteSupplicationItem(keyFavoriteSupplication: String, stateFavoriteSupplication: Boolean) {
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