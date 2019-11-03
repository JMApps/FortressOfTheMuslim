package jmapps.fortressofthemuslim.presentation.mvp.other

import android.content.Context
import android.content.Intent
import androidx.core.net.toUri
import jmapps.fortressofthemuslim.R

class OtherPresenterImpl(
    private val context: Context?,
    private val otherView: OtherContract.OtherView?) :
    OtherContract.OtherPresenter {

    private val linkDescription = context?.getString(R.string.app_name)
    private val linkApp = "https://play.google.com/store/apps/details?id=jmapps.fortressofthemuslum"

    override fun setSettings() {
        otherView?.getSettings()
    }

    override fun setDownloadAll() {
        otherView?.getDownloadAll()
    }

    override fun setNightMode(state: Boolean) {
        otherView?.getNightMode(state)
        otherView?.isNightMode(state)
    }

    override fun setAboutUs() {
        otherView?.getAboutUs()
    }

    override fun rateApp() {
        val rate = Intent(Intent.ACTION_VIEW)
        rate.data = linkApp.toUri()
        context?.startActivity(rate)
    }

    override fun shareLink() {
        val shareLink = Intent(Intent.ACTION_SEND)
        shareLink.type = "text/plain"
        shareLink.putExtra(Intent.EXTRA_TEXT, "$linkDescription\n$linkApp")
        context?.startActivity(shareLink)
    }
}