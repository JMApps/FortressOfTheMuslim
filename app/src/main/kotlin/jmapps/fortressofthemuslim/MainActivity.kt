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

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
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
            
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
}