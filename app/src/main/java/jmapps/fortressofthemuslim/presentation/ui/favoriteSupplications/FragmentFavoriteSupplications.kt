package jmapps.fortressofthemuslim.presentation.ui.favoriteSupplications

import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import jmapps.fortressofthemuslim.R
import jmapps.fortressofthemuslim.data.database.DatabaseContents
import jmapps.fortressofthemuslim.data.database.DatabaseOpenHelper
import kotlinx.android.synthetic.main.fragment_favorite_supplications.view.*

class FragmentFavoriteSupplications: Fragment() {

    private lateinit var rootFavoriteSupplications: View

    private lateinit var database: SQLiteDatabase
    private lateinit var favoriteSupplicationList: MutableList<ModelFavoriteSupplications>
    private lateinit var adapterFavoriteSupplications: AdapterFavoriteSupplications

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        rootFavoriteSupplications = inflater.inflate(R.layout.fragment_favorite_supplications, container, false)

        database = DatabaseOpenHelper(context).readableDatabase
        favoriteSupplicationList = DatabaseContents(context).getFavoriteSupplicationList

        val verticalLayout = LinearLayoutManager(context)
        rootFavoriteSupplications.rvFavoriteSupplications.layoutManager = verticalLayout

        adapterFavoriteSupplications = AdapterFavoriteSupplications(favoriteSupplicationList)
        rootFavoriteSupplications.rvFavoriteSupplications.adapter = adapterFavoriteSupplications

        if (adapterFavoriteSupplications.itemCount <= 0) {
            rootFavoriteSupplications.tvIsFavoriteListEmpty.visibility = View.VISIBLE
            rootFavoriteSupplications.rvFavoriteSupplications.visibility = View.GONE
        } else {
            rootFavoriteSupplications.tvIsFavoriteListEmpty.visibility = View.GONE
            rootFavoriteSupplications.rvFavoriteSupplications.visibility = View.VISIBLE
        }

        return rootFavoriteSupplications
    }
}