package jmapps.fortressofthemuslim.presentation.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri.fromParts
import android.os.Bundle
import android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.preference.PreferenceManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.navigation.NavigationView
import jmapps.fortressofthemuslim.R
import jmapps.fortressofthemuslim.data.files.ManagerPermissions
import jmapps.fortressofthemuslim.presentation.mvp.other.OtherContract
import jmapps.fortressofthemuslim.presentation.mvp.other.OtherPresenterImpl
import jmapps.fortressofthemuslim.presentation.ui.about.BottomSheetAboutUs
import jmapps.fortressofthemuslim.presentation.ui.chapters.FragmentChapters
import jmapps.fortressofthemuslim.presentation.ui.settings.BottomSheetSettings
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
    BottomNavigationView.OnNavigationItemReselectedListener, OtherContract.OtherView {

    private lateinit var preferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    private lateinit var fragmentTransaction: FragmentTransaction
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
        replaceFragment(FragmentChapters())

        preferences = PreferenceManager.getDefaultSharedPreferences(this)
        editor = preferences.edit()

        otherPresenterImpl = OtherPresenterImpl(this, this)
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
        bottomNavigationMain.setOnNavigationItemReselectedListener(this)


        navigationViewMain.menu.findItem(R.id.nav_night_mode).actionView = Switch(this)
        swNightMode = navigationViewMain.menu.findItem(R.id.nav_night_mode).actionView as Switch
        swNightMode.isClickable = false
        swNightMode.isChecked = valNightMode

        managerPermissions = ManagerPermissions(this, permissionsRequestCode)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
//        when (requestCode) {
//            permissionsRequestCode -> {
//                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
//                    setToast(getString(R.string.permissions_failure))
//                } else {
//                    otherPresenterImpl.setDownloadAll()
//                }
//            }
//        }
        for (perms: String in permissions) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                setToast(getString(R.string.permissions_failure))
            } else {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    otherPresenterImpl.setDownloadAll()
                } else {
                    val intent = Intent()
                    intent.action = ACTION_APPLICATION_DETAILS_SETTINGS
                    val uri = fromParts("package", packageName, null)
                    intent.data = uri
                    startActivity(intent)
                }
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_chapters, menu)
        return true
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {

            R.id.nav_settings -> otherPresenterImpl.setSettings()

            R.id.nav_download_all -> {
                if (managerPermissions.checkPermissions()) {
                    otherPresenterImpl.setDownloadAll()
                }
            }

            R.id.nav_night_mode -> otherPresenterImpl.setNightMode(!swNightMode.isChecked)

            R.id.nav_about_us -> otherPresenterImpl.setAboutUs()

            R.id.nav_rate -> otherPresenterImpl.rateApp()

            R.id.nav_share -> otherPresenterImpl.shareLink()
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onNavigationItemReselected(item: MenuItem) {
        when (item.itemId) {

            R.id.bottom_nav_chapters -> replaceFragment(FragmentChapters())

            R.id.bottom_nav_favorite_chapters -> replaceFragment(FragmentChapters())

            R.id.bottom_nav_favorite_supplications -> replaceFragment(FragmentChapters())

            R.id.bottom_nav_supplications -> replaceFragment(FragmentChapters())
        }
        return
    }

    override fun getSettings() {
        val settings = BottomSheetSettings()
        settings.setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.BottomSheetStyleFull)
        settings.show(supportFragmentManager, "settings")
    }

    override fun getDownloadAll() {
        setToast("Началось скачивание...")
    }

    override fun getNightMode(state: Boolean) {
        isNightMode(state)
        swNightMode.isChecked = state
        editor.putBoolean("key_night_mode", state).apply()
    }

    override fun isNightMode(state: Boolean) {
        if (state) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    override fun getAboutUs() {
        val aboutUs = BottomSheetAboutUs()
        aboutUs.setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.BottomSheetStyleFull)
        aboutUs.show(supportFragmentManager, "about_us")
    }

    override fun setToast(message: String) {
        val toast = Toast.makeText(this, message, Toast.LENGTH_LONG)
        val view: View = toast.view
        view.setBackgroundResource(R.drawable.circle_toast_background)
        val text = view.findViewById(android.R.id.message) as TextView
        text.setPadding(32, 16, 32, 16)
        text.setTextColor(resources.getColor(R.color.white))
        toast.show()
    }

    override fun replaceFragment(fragment: Fragment) {
        fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.mainFragmentContainer, fragment)
        fragmentTransaction.commit()
    }
}