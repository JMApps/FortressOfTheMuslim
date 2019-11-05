package jmapps.fortressofthemuslim.presentation.ui.supplications

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
import kotlinx.android.synthetic.main.fragment_supplications.view.*

class FragmentSupplications: Fragment() {

    private lateinit var rootSupplications: View

    private lateinit var database: SQLiteDatabase
    private lateinit var supplicationList: MutableList<ModelSupplications>
    private lateinit var adapterSupplications: AdapterSupplications

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        rootSupplications = inflater.inflate(R.layout.fragment_supplications, container, false)

        database = DatabaseOpenHelper(context).readableDatabase
        supplicationList = DatabaseContents(context).getSupplicationList

        val verticalLayout = LinearLayoutManager(context)
        rootSupplications.rvMainSupplications.layoutManager = verticalLayout

        adapterSupplications = AdapterSupplications(supplicationList)
        rootSupplications.rvMainSupplications.adapter = adapterSupplications

        return rootSupplications
    }
}