package jmapps.fortressofthemuslim.presentation.mvp.main

import android.content.Context
import android.graphics.Color
import android.view.View
import android.widget.TextView
import android.widget.Toast
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
}