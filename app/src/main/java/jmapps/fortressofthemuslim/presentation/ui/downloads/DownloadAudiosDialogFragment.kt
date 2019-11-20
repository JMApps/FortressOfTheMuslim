package jmapps.fortressofthemuslim.presentation.ui.downloads

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import jmapps.fortressofthemuslim.R
import jmapps.fortressofthemuslim.data.files.DownloadManager

class DownloadAudiosDialogFragment : DialogFragment() {

    private lateinit var rootDownloads: View
    private lateinit var downloadManager: DownloadManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {

        rootDownloads = inflater.inflate(R.layout.fragment_download_audios, container, false)

        downloadManager = DownloadManager(context)

        return rootDownloads
    }
}