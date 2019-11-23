package jmapps.fortressofthemuslim.presentation.ui.favoriteSupplications

import android.annotation.SuppressLint
import android.content.*
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
import jmapps.fortressofthemuslim.presentation.mvp.main.MainContract
import jmapps.fortressofthemuslim.presentation.mvp.main.MainPresenterImpl
import kotlinx.android.synthetic.main.fragment_favorite_supplications.view.*

class FragmentFavoriteSupplications : Fragment(), AdapterFavoriteSupplications.AddRemoveFavoriteSupplication,
    ContractFavoriteSupplications.ViewFavoriteSupplications, AdapterFavoriteSupplications.ItemShare,
    AdapterFavoriteSupplications.ItemCopy, MainContract.MainView {

    private lateinit var rootFavoriteSupplications: View

    private lateinit var preferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    private lateinit var database: SQLiteDatabase
    private lateinit var favoriteSupplicationList: MutableList<ModelFavoriteSupplications>
    private lateinit var adapterFavoriteSupplications: AdapterFavoriteSupplications

    private lateinit var mainPresenterImpl: MainPresenterImpl
    private lateinit var favoriteSupplicationPresenterImpl: FavoriteSupplicationPresenterImpl

    private var clipboard: ClipboardManager? = null
    private var clip: ClipData? = null

    @SuppressLint("CommitPrefEdits")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootFavoriteSupplications = inflater.inflate(R.layout.fragment_favorite_supplications, container, false)

        preferences = PreferenceManager.getDefaultSharedPreferences(context)
        editor = preferences.edit()

        database = DatabaseOpenHelper(context).readableDatabase
        favoriteSupplicationList = DatabaseContents(context).getFavoriteSupplicationList

        val verticalLayout = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rootFavoriteSupplications.rvFavoriteSupplications.layoutManager = verticalLayout

        adapterFavoriteSupplications = AdapterFavoriteSupplications(
            favoriteSupplicationList, this, preferences, this, this)
        rootFavoriteSupplications.rvFavoriteSupplications.adapter = adapterFavoriteSupplications

        if (adapterFavoriteSupplications.itemCount <= 0) {
            rootFavoriteSupplications.tvIsFavoriteListEmpty.visibility = View.VISIBLE
            rootFavoriteSupplications.rvFavoriteSupplications.visibility = View.GONE
        } else {
            rootFavoriteSupplications.tvIsFavoriteListEmpty.visibility = View.GONE
            rootFavoriteSupplications.rvFavoriteSupplications.visibility = View.VISIBLE
        }

        mainPresenterImpl = MainPresenterImpl(this, context)
        favoriteSupplicationPresenterImpl = FavoriteSupplicationPresenterImpl(this, database)

        return rootFavoriteSupplications
    }

    override fun addRemoveSupplication(state: Boolean, favoriteSupplicationId: Int) {
        favoriteSupplicationPresenterImpl.addRemoveFavoriteSupplication(state, favoriteSupplicationId)
    }

    override fun showFavoriteSupplicationStateToast(state: Boolean) {
        if (state) {
            mainPresenterImpl.setToastMessage(getString(R.string.favorite_supplication_add))
        } else {
            mainPresenterImpl.setToastMessage(getString(R.string.favorite_supplication_removed))
        }
    }

    override fun showDBExceptionSupplicationToast(error: String) {
        mainPresenterImpl.setToastMessage(getString(R.string.database_exception) + error)
    }

    override fun saveCurrentFavoriteSupplicationItem(keyFavoriteSupplication: String, stateFavoriteSupplication: Boolean) {
        editor.putBoolean(keyFavoriteSupplication, stateFavoriteSupplication).apply()
    }

    override fun copy(content: String) {
        clipboard = context?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager?
        clip = ClipData.newPlainText("", Html.fromHtml(content))
        clipboard?.setPrimaryClip(clip!!)
        mainPresenterImpl.setToastMessage(getString(R.string.copied_to_clipboard))
    }

    override fun share(content: String) {
        val shareLink = Intent(Intent.ACTION_SEND)
        shareLink.type = "text/plain"
        shareLink.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(content).toString())
        context?.startActivity(shareLink)
    }
}