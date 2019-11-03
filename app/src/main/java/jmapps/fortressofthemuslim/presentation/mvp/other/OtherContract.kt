package jmapps.fortressofthemuslim.presentation.mvp.other

interface OtherContract {

    interface OtherView {

        fun getSettings()

        fun getDownloadAll()

        fun getNightMode(state: Boolean)

        fun isNightMode(state: Boolean)

        fun getAboutUs()
    }

    interface OtherPresenter {

        fun setSettings()

        fun setDownloadAll()

        fun setNightMode(state: Boolean)

        fun setAboutUs()

        fun rateApp()

        fun shareLink()
    }
}