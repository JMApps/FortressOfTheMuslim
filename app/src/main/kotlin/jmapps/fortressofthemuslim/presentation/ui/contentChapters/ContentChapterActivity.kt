package jmapps.fortressofthemuslim.presentation.ui.contentChapters

import android.Manifest
import android.annotation.SuppressLint
import android.content.*
import android.content.pm.PackageManager
import android.database.sqlite.SQLiteDatabase
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.text.Html
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.CompoundButton
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.DialogFragment
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import jmapps.fortressofthemuslim.R
import jmapps.fortressofthemuslim.data.database.DatabaseContents
import jmapps.fortressofthemuslim.data.database.DatabaseLists
import jmapps.fortressofthemuslim.data.database.DatabaseOpenHelper
import jmapps.fortressofthemuslim.data.files.ManagerPermissions
import jmapps.fortressofthemuslim.presentation.mvp.favoriteChapters.ContractFavoriteChapters
import jmapps.fortressofthemuslim.presentation.mvp.favoriteChapters.FavoriteChapterPresenterImpl
import jmapps.fortressofthemuslim.presentation.mvp.favoriteSupplications.ContractFavoriteSupplications
import jmapps.fortressofthemuslim.presentation.mvp.favoriteSupplications.FavoriteSupplicationPresenterImpl
import jmapps.fortressofthemuslim.presentation.mvp.main.MainContract
import jmapps.fortressofthemuslim.presentation.mvp.main.MainPresenterImpl
import jmapps.fortressofthemuslim.presentation.ui.chapters.ModelChapters
import jmapps.fortressofthemuslim.presentation.ui.downloads.DownloadAudiosBottomSheet
import kotlinx.android.synthetic.main.activity_content_chapter.*
import kotlinx.android.synthetic.main.content_chapter.*
import java.io.File

class ContentChapterActivity : AppCompatActivity(), AdapterChapterContents.ItemShare,
    AdapterChapterContents.ItemCopy, ContractFavoriteSupplications.ViewFavoriteSupplications,
    AdapterChapterContents.AddRemoveFavoriteSupplication,
    ContractFavoriteChapters.ViewFavoriteChapters, MainContract.MainView,
    AdapterChapterContents.PlayItemClick, View.OnClickListener,
    CompoundButton.OnCheckedChangeListener, SeekBar.OnSeekBarChangeListener,
    MediaPlayer.OnCompletionListener {

    private var chapterId: Int? = null

    private lateinit var preferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    private val permissionsRequestCode = 123
    private lateinit var managerPermissions: ManagerPermissions

    private lateinit var chapterListName: MutableList<ModelChapters>

    private lateinit var database: SQLiteDatabase
    private lateinit var chapterContentList: MutableList<ModelChapterContents>
    private lateinit var adapterChapterContents: AdapterChapterContents

    private lateinit var mainPresenterImpl: MainPresenterImpl
    private lateinit var favoriteChapterPresenterImpl: FavoriteChapterPresenterImpl
    private lateinit var favoriteSupplicationPresenterImpl: FavoriteSupplicationPresenterImpl

    private var clipboard: ClipboardManager? = null
    private var clip: ClipData? = null

    private lateinit var contentNumber: MenuItem

    private var player: MediaPlayer? = null
    private lateinit var runnable: Runnable
    private var handler: Handler = Handler()

    private var previousIndex: Int = 1
    private var nextIndex: Int = 1
    private var trackIndex: Int = 1
    private var trackPosition: Int = 0

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

        managerPermissions = ManagerPermissions(this, permissionsRequestCode)

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

        btnPrevious.setOnClickListener(this)
        tbPlayPause.setOnCheckedChangeListener(this)
        btnNext.setOnClickListener(this)
        sbAudioProgress.setOnSeekBarChangeListener(this)
        tbSequent.setOnCheckedChangeListener(this)
        tbLoop.setOnCheckedChangeListener(this)

        for (i in 0 until chapterContentList.size) {
            previousIndex = chapterContentList[i].chapterContentId!!
            trackIndex = chapterContentList[i].chapterContentId!!
            break
        }

        for (i in 0 until chapterContentList.size) {
            nextIndex = chapterContentList[i].chapterContentId!!
        }

        tbSequent.isClickable = chapterContentList.size > 1
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
        isShowDownload()
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
                    contentNumber.isChecked, chapterId!!
                )
            }

            R.id.action_content_download -> {
                val downloadAudiosBottomSheet = DownloadAudiosBottomSheet()
                downloadAudiosBottomSheet.setStyle(DialogFragment.STYLE_NORMAL, R.style.BottomSheetStyleFull)
                downloadAudiosBottomSheet.show(supportFragmentManager, "download_audio")
            }

            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (player != null) {
            handler.removeCallbacks(runnable)
        }
        clear()
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
                    if (player == null) {
                        initPlayer(trackIndex)
                        player?.start()
                    } else {
                        adapterChapterContents.onItemSelected(trackPosition)
                        currentAudioProgress()
                        player?.start()
                    }
                } else {
                    adapterChapterContents.onItemSelected(-1)
                    player?.pause()
                }
            }

            R.id.tbLoop -> {
                if (isChecked) {
                    if (tbSequent.isChecked) {
                        tbSequent.isChecked = false
                    }
                    mainPresenterImpl.setToastMessage(getString(R.string.player_loop_on))
                } else {
                    mainPresenterImpl.setToastMessage(getString(R.string.player_loop_off))
                }
                player?.isLooping = isChecked
            }

            R.id.tbSequent -> {
                if (isChecked) {
                    if (tbLoop.isChecked) {
                        tbLoop.isChecked = false
                    }
                    mainPresenterImpl.setToastMessage(getString(R.string.player_sequent_play_on))
                } else {
                    mainPresenterImpl.setToastMessage(getString(R.string.player_sequent_play_off))
                }
            }
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {

            R.id.btnPrevious -> {
                if (trackIndex > previousIndex) {
                    trackIndex--
                    trackPosition--
                    initPlayer(trackIndex)
                    player?.start()
                    tbPlayPause.isChecked = true
                }
            }

            R.id.btnNext -> {
                if (trackIndex < nextIndex) {
                    trackIndex++
                    trackPosition++
                    initPlayer(trackIndex)
                    player?.start()
                    tbPlayPause.isChecked = true
                }
            }
        }
    }

    override fun playItem(supplicationId: Int, position: Int) {
        trackIndex = supplicationId
        trackPosition = position
        tbPlayPause.isChecked = supplicationId != -1
        if (supplicationId != -1) {
            initPlayer(trackIndex)
            player?.start()
        } else {
            clear()
            handler.removeCallbacks(runnable)
            sbAudioProgress?.progress = 0
        }
    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        if (fromUser) {
            player?.seekTo(progress * 1000)
        }
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {}

    override fun onStopTrackingTouch(seekBar: SeekBar?) {}

    override fun onCompletion(mp: MediaPlayer?) {
        if (tbLoop.isChecked) {
            player?.isLooping = true
        } else {
            player?.isLooping = false
            if (tbSequent.isChecked) {
                if (trackPosition < chapterContentList.size - 1) {
                    trackIndex++
                    trackPosition++
                    initPlayer(trackIndex)
                    player?.start()
                } else {
                    for (i in 0 until chapterContentList.size) {
                        trackIndex = chapterContentList[i].chapterContentId!!
                        break
                    }
                    trackPosition = 0
                    rvChapterContent.smoothScrollToPosition(0)
                    adapterChapterContents.onItemSelected(- 1)
                    tbPlayPause.isChecked = false
                    tbSequent.isChecked = false
                    handler.removeCallbacks(runnable)
                    sbAudioProgress?.progress = 0
                    player = null
                }
            } else {
                tbPlayPause.isChecked = false
                adapterChapterContents.onItemSelected(- 1)
                handler.removeCallbacks(runnable)
                sbAudioProgress?.progress = 0
            }
        }
    }

    private fun initPlayer(index: Int) {
        clear()
        val downloadItem = File(Environment.getExternalStorageDirectory(),
            "/FortressOfTheMuslim_audio/dua$index.mp3").exists()
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            if (downloadItem) {
                val audioFile = File(Environment.getExternalStorageDirectory(), "/FortressOfTheMuslim_audio/dua$index.mp3")
                player = MediaPlayer.create(this, Uri.parse(audioFile.toString()))
                currentAudioProgress()
                rvChapterContent.smoothScrollToPosition(trackPosition)
                adapterChapterContents.onItemSelected(trackPosition)
                player?.setOnCompletionListener(this)
            } else {
                handler.removeCallbacks(runnable)
                clear()
                mainPresenterImpl.setToastMessage(getString(R.string.player_is_track_null))
            }
        } else {
            managerPermissions.checkPermissions()
        }
    }

    private fun currentAudioProgress() {
        sbAudioProgress?.max = player?.seconds!!
        runnable = Runnable {
            sbAudioProgress?.progress = player?.currentSeconds!!
            handler.postDelayed(runnable, 1000)
        }
        handler.postDelayed(runnable, 1000)
    }

    private fun clear() {
        player?.stop()
        player?.release()
        player = null
    }

    private val MediaPlayer.seconds: Int?
        get() {
            return this.duration / 1000
        }

    private val MediaPlayer.currentSeconds: Int?
        get() {
            return this.currentPosition / 1000
        }

    private fun isShowDownload() {
        for (i in 0 until chapterContentList.size) {
            val downloadItem: String? = chapterContentList[i].strChapterContentArabic
            if (downloadItem.isNullOrEmpty()) {
                bottomPlayerBar.visibility = View.GONE
            } else {
                val isDownloadItem = File(Environment.getExternalStorageDirectory(),
                    "/FortressOfTheMuslim_audio/dua${chapterContentList[i].chapterContentId}.mp3").exists()
                if (isDownloadItem) {
                    bottomPlayerBar.visibility = View.VISIBLE
                } else {
                    bottomPlayerBar.visibility = View.GONE
                }
            }
            break
        }
    }
}