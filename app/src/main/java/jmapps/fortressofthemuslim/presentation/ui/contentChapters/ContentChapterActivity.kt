package jmapps.fortressofthemuslim.presentation.ui.contentChapters

import android.Manifest
import android.annotation.SuppressLint
import android.content.*
import android.content.pm.PackageManager
import android.database.sqlite.SQLiteDatabase
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.text.Html
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.CompoundButton
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import jmapps.fortressofthemuslim.R
import jmapps.fortressofthemuslim.data.database.DatabaseContents
import jmapps.fortressofthemuslim.data.database.DatabaseLists
import jmapps.fortressofthemuslim.data.database.DatabaseOpenHelper
import jmapps.fortressofthemuslim.data.files.DownloadManager
import jmapps.fortressofthemuslim.data.files.ManagerPermissions
import jmapps.fortressofthemuslim.presentation.mvp.favoriteChapters.ContractFavoriteChapters
import jmapps.fortressofthemuslim.presentation.mvp.favoriteChapters.FavoriteChapterPresenterImpl
import jmapps.fortressofthemuslim.presentation.mvp.favoriteSupplications.ContractFavoriteSupplications
import jmapps.fortressofthemuslim.presentation.mvp.favoriteSupplications.FavoriteSupplicationPresenterImpl
import jmapps.fortressofthemuslim.presentation.mvp.main.MainContract
import jmapps.fortressofthemuslim.presentation.mvp.main.MainPresenterImpl
import jmapps.fortressofthemuslim.presentation.ui.chapters.ModelChapters
import kotlinx.android.synthetic.main.activity_content_chapter.*
import kotlinx.android.synthetic.main.content_chapter.*

class ContentChapterActivity : AppCompatActivity(), AdapterChapterContents.ItemShare,
    AdapterChapterContents.ItemCopy, ContractFavoriteSupplications.ViewFavoriteSupplications,
    AdapterChapterContents.AddRemoveFavoriteSupplication,
    ContractFavoriteChapters.ViewFavoriteChapters, MainContract.MainView,
    AdapterChapterContents.PlayItemClick, View.OnClickListener,
    CompoundButton.OnCheckedChangeListener, SeekBar.OnSeekBarChangeListener {

    private var chapterId: Int? = null

    private lateinit var preferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    private lateinit var chapterListName: MutableList<ModelChapters>

    private lateinit var database: SQLiteDatabase
    private lateinit var chapterContentList: MutableList<ModelChapterContents>
    private lateinit var adapterChapterContents: AdapterChapterContents

    private lateinit var mainPresenterImpl: MainPresenterImpl
    private lateinit var favoriteChapterPresenterImpl: FavoriteChapterPresenterImpl
    private lateinit var favoriteSupplicationPresenterImpl: FavoriteSupplicationPresenterImpl

    private val permissionsRequestCode = 123
    private lateinit var managerPermissions: ManagerPermissions

    private lateinit var downloadManager: DownloadManager

    private var clipboard: ClipboardManager? = null
    private var clip: ClipData? = null

    private lateinit var contentNumber: MenuItem

    private var player: MediaPlayer? = null
    private lateinit var runnable: Runnable
    private var handler: Handler = Handler()
    private var trackIndex: Int = 0

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

        val verticalLayout = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvChapterContent.layoutManager = verticalLayout

        chapterContentList = DatabaseContents(this).getChapterContentList(chapterId)
        adapterChapterContents = AdapterChapterContents(
            chapterContentList, this, this, preferences, this, this)
        rvChapterContent.adapter = adapterChapterContents

        mainPresenterImpl = MainPresenterImpl(this, this)
        favoriteChapterPresenterImpl = FavoriteChapterPresenterImpl(this, database)
        favoriteSupplicationPresenterImpl = FavoriteSupplicationPresenterImpl(this, database)

        downloadManager = DownloadManager(this)
        managerPermissions = ManagerPermissions(this, permissionsRequestCode)

        btnPrevious.setOnClickListener(this)
        tbPlayPause.setOnCheckedChangeListener(this)
        btnNext.setOnClickListener(this)
        sbAudioProgress.setOnSeekBarChangeListener(this)
        tbSequent.setOnCheckedChangeListener(this)
        tbLoop.setOnCheckedChangeListener(this)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        for (perms: String in permissions) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                mainPresenterImpl.setToastMessage(getString(R.string.permissions_failure))
            } else {
                if (ActivityCompat.checkSelfPermission(
                        this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    downloadManager.downloadAllAudios()
                } else {
                    val intent = Intent()
                    intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                    val uri = Uri.fromParts("package", packageName, null)
                    intent.data = uri
                    startActivity(intent)
                }
            }
        }
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
                favoriteChapterPresenterImpl.addRemoveFavoriteChapter(
                    contentNumber.isChecked,
                    chapterId!!
                )
            }

            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun copy(content: String) {
        clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager?
        clip = ClipData.newPlainText("", Html.fromHtml(content))
        clipboard?.setPrimaryClip(clip!!)
        mainPresenterImpl.setToastMessage(getString(R.string.copied_to_clipboard))
    }

    override fun share(content: String) {
        val shareLink = Intent(Intent.ACTION_SEND)
        shareLink.type = "text/plain"
        shareLink.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(content))
        startActivity(shareLink)
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

    override fun addRemoveSupplication(state: Boolean, chapterContentId: Int) {
        favoriteSupplicationPresenterImpl.addRemoveFavoriteSupplication(state, chapterContentId)
    }

    override fun showFavoriteSupplicationStateToast(state: Boolean) {
        if (state) {
            mainPresenterImpl.setToastMessage(getString(R.string.favorite_supplication_add))
        } else {
            mainPresenterImpl.setToastMessage(getString(R.string.favorite_supplication_removed))
        }
    }

    override fun showDBExceptionSupplicationToast(error: String) {
        mainPresenterImpl.setToastMessage(getString(R.string.database_exception) + error)
    }

    override fun saveCurrentFavoriteSupplicationItem(keyFavoriteSupplication: String, stateFavoriteSupplication: Boolean) {
        editor.putBoolean(keyFavoriteSupplication, stateFavoriteSupplication).apply()
    }

    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        when (buttonView?.id) {

            R.id.tbPlayPause -> {
                if (isChecked) {
                    mainPresenterImpl.setToastMessage("Play")
                } else {
                    mainPresenterImpl.setToastMessage("Pause")
                }
            }

            R.id.tbLoop -> {
                if (isChecked) {
                    mainPresenterImpl.setToastMessage("Loop on")
                } else {
                    mainPresenterImpl.setToastMessage("Loop off")
                }
            }

            R.id.tbSequent -> {
                if (isChecked) {
                    mainPresenterImpl.setToastMessage("Sequent on")
                } else {
                    mainPresenterImpl.setToastMessage("Sequent off")
                }
            }
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {

            R.id.btnPrevious -> {
                mainPresenterImpl.setToastMessage("Previous track")
            }

            R.id.btnNext -> {
                mainPresenterImpl.setToastMessage("Next track")
            }
        }
    }

    override fun playItem(supplicationId: Int) {
        mainPresenterImpl.setToastMessage("Play item = $supplicationId")
    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        mainPresenterImpl.setToastMessage("Progress = $progress")
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {}

    override fun onStopTrackingTouch(seekBar: SeekBar?) {}
}