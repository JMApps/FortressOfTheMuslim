package jmapps.fortressofthemuslim.presentation.mvp.main

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.provider.Settings
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import jmapps.fortressofthemuslim.R

class MainPresenterImpl(
    private val mainView: MainContract.MainView?,
    private val context: Context?) : MainContract.MainPresenter {

    override fun setToastMessage(message: String) {
        val toast = Toast.makeText(context, message, Toast.LENGTH_LONG)

        val view: View = toast.view
        view.setBackgroundResource(R.drawable.circle_toast_background)

        val text = view.findViewById(android.R.id.message) as TextView
        text.setPadding(32, 16, 32, 16)
        text.setTextColor(Color.WHITE)

        toast.show()
    }

    override fun setAlertDialog() {
        val builder = AlertDialog.Builder(context!!)
        builder.setIcon(R.drawable.ic_warning_accent)
        builder.setTitle(R.string.permissions_waring)
        builder.setMessage(R.string.permissions_text)
        builder.setPositiveButton(R.string.permissions_ok) { _, _ -> toSettingsApp() }
        builder.setNeutralButton(R.string.permissions_cancel, null)
        val dialog = builder.create()
        dialog.show()
    }

    private fun toSettingsApp() {
        val intent = Intent()
        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        val uri = Uri.fromParts("package", context?.packageName, null)
        intent.data = uri
        context?.startActivity(intent)
    }
}