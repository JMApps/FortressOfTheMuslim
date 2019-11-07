package jmapps.fortressofthemuslim.presentation.mvp.other

import androidx.fragment.app.Fragment

interface OtherContract {

    interface OtherView {

        fun getSettings()

        fun getDownloadAll()

        fun getNightMode(state: Boolean)

        fun isNightMode(state: Boolean)

        fun getAboutUs()

        fun replaceFragment(fragment: Fragment)
    }

    interface OtherPresenter {

        fun setSettings()

        fun setDownloadAll()

        fun setNightMode(state: Boolean)

        fun setAboutUs()

        fun rateApp()

        fun shareLink()

        fun replaceFragment(fragment: Fragment)
    }
}