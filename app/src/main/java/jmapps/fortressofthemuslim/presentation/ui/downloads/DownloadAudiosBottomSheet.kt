package jmapps.fortressofthemuslim.presentation.ui.downloads

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import jmapps.fortressofthemuslim.R
import jmapps.fortressofthemuslim.data.database.DatabaseLists
import jmapps.fortressofthemuslim.data.files.DownloadManager
import jmapps.fortressofthemuslim.data.files.ManagerPermissions
import jmapps.fortressofthemuslim.presentation.mvp.main.MainContract
import jmapps.fortressofthemuslim.presentation.mvp.main.MainPresenterImpl
import kotlinx.android.synthetic.main.fragment_download_audios.view.*

class DownloadAudiosBottomSheet : BottomSheetDialogFragment(), View.OnClickListener,
    MainContract.MainView {

    private lateinit var rootDownloads: View

    private val permissionsRequestCode = 123
    private lateinit var managerPermissions: ManagerPermissions

    private lateinit var downloadManager: DownloadManager
    private lateinit var mainPresenterImpl: MainPresenterImpl

    private lateinit var downloadSelectivelyList: MutableList<ModelDownloadSelectively>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {

        downloadSelectivelyList = DatabaseLists(context).getChapterNameSelectively

        rootDownloads = inflater.inflate(R.layout.fragment_download_audios, container, false)

        managerPermissions = ManagerPermissions(activity!!, permissionsRequestCode)
        mainPresenterImpl = MainPresenterImpl(this, context)
        downloadManager = DownloadManager(context)

        rootDownloads.btnDownloadAll.setOnClickListener(this)
        rootDownloads.btnDownloadSelectively.setOnClickListener(this)

        return rootDownloads
    }

    override fun onClick(v: View?) {
        when(v?.id) {

            R.id.btnDownloadAll -> {
                if (managerPermissions.checkPermissions()) {
                    downloadManager.downloadAllAudios()
                }
            }

            R.id.btnDownloadSelectively -> {
                mainPresenterImpl.setToastMessage(getString(R.string.download_finalized))
//                val downloadSelectivelyBottomSheet = DownloadSelectivelyBottomSheet()
//                downloadSelectivelyBottomSheet.setStyle(STYLE_NORMAL, R.style.BottomSheetStyleFull)
//                downloadSelectivelyBottomSheet.show(activity!!.supportFragmentManager, "download_selectively")
            }
        }
    }
}