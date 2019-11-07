package jmapps.fortressofthemuslim.presentation.ui.supplications

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipData.newPlainText
import android.content.ClipboardManager
import android.content.Context.CLIPBOARD_SERVICE
import android.content.Intent
import android.content.SharedPreferences
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import jmapps.fortressofthemuslim.R
import jmapps.fortressofthemuslim.data.database.DatabaseContents
import jmapps.fortressofthemuslim.data.database.DatabaseOpenHelper
import jmapps.fortressofthemuslim.presentation.mvp.favoriteSupplications.ContractFavoriteSupplications
import jmapps.fortressofthemuslim.presentation.mvp.favoriteSupplications.FavoriteSupplicationPresenterImpl
import kotlinx.android.synthetic.main.fragment_supplications.view.*

class FragmentSupplications: Fragment(), ContractFavoriteSupplications.ViewFavoriteSupplications,
    AdapterSupplications.AddRemoveFavoriteSupplication, AdapterSupplications.ItemShare,
    AdapterSupplications.ItemCopy {

    private lateinit var rootSupplications: View

    private lateinit var preferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    private lateinit var database: SQLiteDatabase
    private lateinit var supplicationList: MutableList<ModelSupplications>
    private lateinit var adapterSupplications: AdapterSupplications

    private lateinit var favoriteSupplicationPresenterImpl: FavoriteSupplicationPresenterImpl

    private var clipboard: ClipboardManager? = null
    private var clip: ClipData? = null

    @SuppressLint("CommitPrefEdits")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        rootSupplications = inflater.inflate(R.layout.fragment_supplications, container, false)

        preferences = PreferenceManager.getDefaultSharedPreferences(context)
        editor = preferences.edit()

        database = DatabaseOpenHelper(context).readableDatabase
        supplicationList = DatabaseContents(context).getSupplicationList

        val verticalLayout = LinearLayoutManager(context)
        rootSupplications.rvMainSupplications.layoutManager = verticalLayout

        adapterSupplications = AdapterSupplications(
            supplicationList, this, preferences, this, this)
        rootSupplications.rvMainSupplications.adapter = adapterSupplications

        favoriteSupplicationPresenterImpl = FavoriteSupplicationPresenterImpl(this, database)

        return rootSupplications
    }

    override fun addRemoveSupplication(state: Boolean, supplicationId: Int) {
        favoriteSupplicationPresenterImpl.addRemoveFavoriteSupplication(state, supplicationId)
    }

    override fun showFavoriteSupplicationStateToast(state: Boolean) {
        if (state) {
            setToast(getString(R.string.favorite_add))
        } else {
            setToast(getString(R.string.favorite_removed))
        }
    }

    override fun showDBExceptionSupplicationToast(error: String) {
        setToast(getString(R.string.database_exception) + error)
    }

    override fun saveCurrentFavoriteSupplicationItem(keyFavoriteSupplication: String, stateFavoriteSupplication: Boolean) {
        editor.putBoolean(keyFavoriteSupplication, stateFavoriteSupplication).apply()
    }

    override fun copy(content: String) {
        clipboard = context?.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager?
        clip = newPlainText("", Html.fromHtml(content))
        clipboard?.setPrimaryClip(clip!!)
        setToast(getString(R.string.copied_to_clipboard))
    }

    override fun share(content: String) {
        val shareLink = Intent(Intent.ACTION_SEND)
        shareLink.type = "text/plain"
        shareLink.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(content))
        context?.startActivity(shareLink)
    }

    private fun setToast(message: String) {
        val toast = Toast.makeText(context, message, Toast.LENGTH_LONG)
        val view: View = toast.view
        view.setBackgroundResource(R.drawable.circle_toast_background)
        val text = view.findViewById(android.R.id.message) as TextView
        text.setPadding(32, 16, 32, 16)
        text.setTextColor(resources.getColor(R.color.white))
        toast.show()
    }
}