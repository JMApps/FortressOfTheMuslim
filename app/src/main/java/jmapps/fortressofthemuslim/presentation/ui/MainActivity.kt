package jmapps.fortressofthemuslim.presentation.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.MenuItem
import android.widget.Switch
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.view.GravityCompat
import androidx.fragment.app.DialogFragment.STYLE_NORMAL
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.preference.PreferenceManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.navigation.NavigationView
import jmapps.fortressofthemuslim.R
import jmapps.fortressofthemuslim.data.files.ManagerPermissions
import jmapps.fortressofthemuslim.presentation.mvp.main.MainContract
import jmapps.fortressofthemuslim.presentation.mvp.main.MainPresenterImpl
import jmapps.fortressofthemuslim.presentation.mvp.other.OtherContract
import jmapps.fortressofthemuslim.presentation.mvp.other.OtherPresenterImpl
import jmapps.fortressofthemuslim.presentation.ui.about.BottomSheetAboutUs
import jmapps.fortressofthemuslim.presentation.ui.chapters.FragmentChapters
import jmapps.fortressofthemuslim.presentation.ui.downloads.DownloadAudiosBottomSheet
import jmapps.fortressofthemuslim.presentation.ui.favoriteChapters.FragmentFavoriteChapters
import jmapps.fortressofthemuslim.presentation.ui.favoriteSupplications.FragmentFavoriteSupplications
import jmapps.fortressofthemuslim.presentation.ui.settings.BottomSheetSettings
import jmapps.fortressofthemuslim.presentation.ui.supplications.FragmentSupplications
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
    OtherContract.OtherView, BottomNavigationView.OnNavigationItemSelectedListener,
    MainContract.MainView {

    private val keyArabicTextColor = "key_arabic_text_color"
    private val keyTranscriptionTextColor = "key_transcription_text_color"
    private val keyTranslationTextColor = "key_translation_text_color"

    private lateinit var preferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    private lateinit var fragmentTransaction: FragmentTransaction
    private lateinit var mainPresenterImpl: MainPresenterImpl
    private lateinit var otherPresenterImpl: OtherPresenterImpl

    private lateinit var swNightMode: Switch
    private var valNightMode: Boolean = false

    private val permissionsRequestCode = 123
    private lateinit var managerPermissions: ManagerPermissions

    @SuppressLint("CommitPrefEdits")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        LockOrientation(this).lock()

        preferences = PreferenceManager.getDefaultSharedPreferences(this)
        editor = preferences.edit()

        otherPresenterImpl = OtherPresenterImpl(this, this)
        mainPresenterImpl = MainPresenterImpl(this, this)
        otherPresenterImpl.replaceFragment(FragmentChapters())

        valNightMode = preferences.getBoolean("key_night_mode", false)
        isNightMode(valNightMode)

        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )

        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        navigationViewMain.setNavigationItemSelectedListener(this)
        bottomNavigationMain.setOnNavigationItemSelectedListener(this)

        navigationViewMain.menu.findItem(R.id.nav_night_mode).actionView = Switch(this)
        swNightMode = navigationViewMain.menu.findItem(R.id.nav_night_mode).actionView as Switch
        swNightMode.isClickable = false
        swNightMode.isChecked = valNightMode

        managerPermissions = ManagerPermissions(this, permissionsRequestCode)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            mainPresenterImpl.setAlertDialog()
        } else {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                val downloadAudiosBottomSheet = DownloadAudiosBottomSheet()
                downloadAudiosBottomSheet.setStyle(STYLE_NORMAL, R.style.BottomSheetStyleFull)
                downloadAudiosBottomSheet.show(supportFragmentManager, "download_audio")
            } else {
                mainPresenterImpl.setAlertDialog()
            }
        }
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {

            R.id.nav_settings -> otherPresenterImpl.setSettings()

            R.id.nav_download_all -> if (managerPermissions.checkPermissions()) {
                val downloadAudiosBottomSheet = DownloadAudiosBottomSheet()
                downloadAudiosBottomSheet.setStyle(STYLE_NORMAL, R.style.BottomSheetStyleFull)
                downloadAudiosBottomSheet.show(supportFragmentManager, "download_audio")
            }

            R.id.nav_night_mode -> otherPresenterImpl.setNightMode(!swNightMode.isChecked)

            R.id.nav_about_us -> otherPresenterImpl.setAboutUs()

            R.id.nav_rate -> otherPresenterImpl.rateApp()

            R.id.nav_share -> otherPresenterImpl.shareLink()

            R.id.bottom_nav_chapters -> otherPresenterImpl.replaceFragment(FragmentChapters())

            R.id.bottom_nav_favorite_chapters -> otherPresenterImpl.replaceFragment(FragmentFavoriteChapters())

            R.id.bottom_nav_favorite_supplications -> otherPresenterImpl.replaceFragment(FragmentFavoriteSupplications())

            R.id.bottom_nav_supplications -> otherPresenterImpl.replaceFragment(FragmentSupplications())
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun getSettings() {
        val settings = BottomSheetSettings()
        settings.setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.BottomSheetStyleFull)
        settings.show(supportFragmentManager, "settings")
    }

    override fun getNightMode(state: Boolean) {
        bottomNavigationMain.selectedItemId = R.id.bottom_nav_chapters
        isNightMode(state)
        swNightMode.isChecked = state
        editor.putBoolean("key_night_mode", state).apply()
    }

    override fun isNightMode(state: Boolean) {
        if (state) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            editor.putInt(keyArabicTextColor, 0)
            editor.putInt(keyTranscriptionTextColor, 0)
            editor.putInt(keyTranslationTextColor, 0)
            editor.apply()
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            editor.putInt(keyArabicTextColor, 1)
            editor.putInt(keyTranscriptionTextColor, 1)
            editor.putInt(keyTranslationTextColor, 1)
            editor.apply()
        }
    }

    override fun getAboutUs() {
        val aboutUs = BottomSheetAboutUs()
        aboutUs.setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.BottomSheetStyleFull)
        aboutUs.show(supportFragmentManager, "about_us")
    }

    override fun replaceFragment(fragment: Fragment) {
        fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.mainFragmentContainer, fragment)
        fragmentTransaction.commit()
    }
}