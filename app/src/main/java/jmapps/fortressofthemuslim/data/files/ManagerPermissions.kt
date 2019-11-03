package jmapps.fortressofthemuslim.data.files

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import jmapps.fortressofthemuslim.R
import jmapps.fortressofthemuslim.presentation.ui.MainActivity

private lateinit var list: List<String>

class ManagerPermissions(
    private val activity: MainActivity,
    private val permissionsRequestCode: Int) {

    init {
        list = listOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    }

    fun checkPermissions(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return if (isPermissionsGranted() != PackageManager.PERMISSION_GRANTED) {
                showAlert()
                false
            } else {
                true
            }
        }
        return true
    }

    private fun isPermissionsGranted(): Int {
        return ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    }

    private fun showAlert() {
        val builder = AlertDialog.Builder(activity)
        builder.setIcon(R.drawable.ic_warning_accent)
        builder.setTitle(R.string.permissions_waring)
        builder.setMessage(R.string.permissions_text)
        builder.setPositiveButton(R.string.permissions_ok) { _, _ -> requestPermissions() }
        builder.setNeutralButton(R.string.permissions_cancel, null)
        val dialog = builder.create()
        dialog.show()
    }

    private fun requestPermissions() {
        val permission = deniedPermission()
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
            ActivityCompat.requestPermissions(activity, list.toTypedArray(), permissionsRequestCode)
        } else {
            ActivityCompat.requestPermissions(activity, list.toTypedArray(), permissionsRequestCode)
        }
    }

    private fun deniedPermission(): String {
        for (permission in list) {
            if (ContextCompat.checkSelfPermission(activity, permission)
                == PackageManager.PERMISSION_DENIED) return permission
        }
        return ""
    }
}