package jmapps.fortressofthemuslim.presentation.ui.downloads

import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import jmapps.fortressofthemuslim.R
import jmapps.fortressofthemuslim.data.database.DatabaseLists
import jmapps.fortressofthemuslim.data.database.DatabaseOpenHelper
import kotlinx.android.synthetic.main.fragment_download_selectively.view.*

class DownloadSelectivelyBottomSheet : BottomSheetDialogFragment(),
    AdapterDownloadSelectively.SelectChapter {

    private lateinit var rootSelectively: View
    private lateinit var database: SQLiteDatabase

    private lateinit var downloadSelectivelyList: MutableList<ModelDownloadSelectively>
    private lateinit var adapterDownloadSelectively: AdapterDownloadSelectively

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootSelectively = inflater.inflate(R.layout.fragment_download_selectively, container, false)

        database = DatabaseOpenHelper(context).readableDatabase
        downloadSelectivelyList = DatabaseLists(context).getChapterNameSelectively

        val verticalLayout = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rootSelectively.rvDownloadSelectively.layoutManager = verticalLayout

        adapterDownloadSelectively = AdapterDownloadSelectively(downloadSelectivelyList, this)
        rootSelectively.rvDownloadSelectively.adapter = adapterDownloadSelectively

        return rootSelectively
    }

    override fun select(chapterId: Int, isChecked: Boolean) {

    }
}