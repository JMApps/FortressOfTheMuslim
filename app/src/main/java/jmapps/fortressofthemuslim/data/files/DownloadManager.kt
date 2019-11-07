package jmapps.fortressofthemuslim.data.files

import android.app.ProgressDialog
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Environment
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.liulishuo.filedownloader.BaseDownloadTask
import com.liulishuo.filedownloader.FileDownloadListener
import com.liulishuo.filedownloader.FileDownloadQueueSet
import com.liulishuo.filedownloader.FileDownloader
import jmapps.fortressofthemuslim.R
import jmapps.fortressofthemuslim.data.database.DatabaseLinkLists
import jmapps.fortressofthemuslim.presentation.ui.supplications.ViewHolderSupplications
import java.io.File

class DownloadManager(private val context: Context?) {

    private lateinit var progressDownload: ProgressDialog
    private lateinit var linkList: List<ModelSupplicationLink>
    private var queueSet = FileDownloadQueueSet(allListener())
    private var tasks = ArrayList<BaseDownloadTask>()

    fun downloadAllAudios() {
        linkList = DatabaseLinkLists(context).getSupplicationLinksList

        progressDownload = ProgressDialog(context)
        progressDownload.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL)
        progressDownload.setIcon(R.drawable.ic_download_accent)
        progressDownload.setTitle(context?.getString(R.string.download_starting))
        progressDownload.setMessage(context?.getString(R.string.download_waiting))
        progressDownload.max = linkList.size
        progressDownload.setCancelable(false)
        progressDownload.show()

        FileDownloader.setup(context)
        FileDownloader.getImpl().bindService()
        FileDownloader.getImpl().clearAllTaskData()

        val pathToMainFolder = File(
            Environment.getExternalStorageDirectory(),
            File.separator + "FortressOfTheMuslim_audio" + File.separator + "dua")

        tasks = ArrayList()

        for (i in linkList.indices) {
            tasks.add(
                FileDownloader.getImpl().create(linkList[i].strSupplicationLink).setPath(
                    "$pathToMainFolder${linkList[i].supplicationLinkId}.mp3").setTag(i + 1))
        }

        if (isNetworkAvailable()) {
            queueSet = FileDownloadQueueSet(allListener())
            queueSet.disableCallbackProgressTimes()
            queueSet.setAutoRetryTimes(1)
            queueSet.downloadSequentially(tasks)
            queueSet.start()
        } else {
            context?.getString(R.string.download_checking_connect)?.let { setToast(it) }
            FileDownloader.getImpl().clearAllTaskData()
            tasks.clear()
            progressDownload.dismiss()
        }
    }

    private fun allListener(): FileDownloadListener {
        return object : FileDownloadListener() {

            override fun pending(task: BaseDownloadTask, soFarBytes: Int, totalBytes: Int) {
                if (!isNetworkAvailable()) {
                    context?.getString(R.string.download_checking_connect)?.let { setToast(it) }
                    FileDownloader.getImpl().clearAllTaskData()
                    tasks.clear()
                    progressDownload.dismiss()
                }
            }

            override fun progress(task: BaseDownloadTask, soFarBytes: Int, totalBytes: Int) {}

            override fun completed(task: BaseDownloadTask) {
                progressDownload.progress = progressDownload.progress + 1
                progressDownload.setTitle(R.string.download_downloading)
                if (progressDownload.progress == linkList.size) {
                    context?.getString(R.string.download_complete)?.let { setToast(it) }
                    progressDownload.dismiss()
                }
            }

            override fun paused(task: BaseDownloadTask, soFarBytes: Int, totalBytes: Int) {
                FileDownloader.getImpl().pauseAll()
                progressDownload.dismiss()
            }

            override fun error(task: BaseDownloadTask, e: Throwable) {
                FileDownloader.getImpl().pauseAll()
                progressDownload.dismiss()
            }

            override fun warn(task: BaseDownloadTask) {
            }
        }
    }

    fun downloadSelectivelyAudios(groupId: Int, adapter: RecyclerView.Adapter<ViewHolderSupplications>?) {

        linkList = DatabaseLinkLists(context).getSupplicationSelectiveList(groupId)

        progressDownload = ProgressDialog(context)
        progressDownload.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL)
        progressDownload.setIcon(R.drawable.ic_download_accent)
        progressDownload.setTitle(context?.getString(R.string.download_starting))
        progressDownload.setMessage(context?.getString(R.string.download_waiting))
        progressDownload.max = linkList.size
        progressDownload.setCancelable(false)
        progressDownload.show()

        FileDownloader.setup(context)
        FileDownloader.getImpl().bindService()
        FileDownloader.getImpl().clearAllTaskData()

        val pathToMainFolder = File(
            Environment.getExternalStorageDirectory(),
            File.separator + "FortressOfTheMuslim_audio" + File.separator + "dua"
        )

        tasks = ArrayList()

        for (i in linkList.indices) {
            tasks.add(
                FileDownloader.getImpl().create(linkList[i].strSupplicationLink).setPath(
                    "$pathToMainFolder${linkList[i].supplicationLinkId}.mp3").setTag(i + 1))
        }

        if (isNetworkAvailable()) {
            val queueSetSelectively = FileDownloadQueueSet(selectivelyListener(adapter!!))
            queueSetSelectively.disableCallbackProgressTimes()
            queueSetSelectively.setAutoRetryTimes(1)
            queueSetSelectively.downloadSequentially(tasks)
            queueSetSelectively.start()
        } else {
            context?.getString(R.string.download_complete)?.let { setToast(it) }
            FileDownloader.getImpl().clearAllTaskData()
            tasks.clear()
            progressDownload.dismiss()
        }
    }

    private fun selectivelyListener(adapter: RecyclerView.Adapter<ViewHolderSupplications>?): FileDownloadListener {
        return object : FileDownloadListener() {

            override fun pending(task: BaseDownloadTask, soFarBytes: Int, totalBytes: Int) {
                if (!isNetworkAvailable()) {
                    context?.getString(R.string.download_complete)?.let { setToast(it) }
                    FileDownloader.getImpl().clearAllTaskData()
                    tasks.clear()
                    progressDownload.dismiss()
                }
            }

            override fun progress(task: BaseDownloadTask, soFarBytes: Int, totalBytes: Int) {}

            override fun completed(task: BaseDownloadTask) {
                progressDownload.progress = progressDownload.progress + 1
                progressDownload.setTitle(R.string.download_downloading)
                if (progressDownload.progress == linkList.size) {
                    context?.getString(R.string.download_complete)?.let { setToast(it) }
                    progressDownload.dismiss()
                }
                adapter?.notifyDataSetChanged()
            }

            override fun paused(task: BaseDownloadTask, soFarBytes: Int, totalBytes: Int) {
                FileDownloader.getImpl().pauseAll()
                progressDownload.dismiss()
            }

            override fun error(task: BaseDownloadTask, e: Throwable) {
                progressDownload.dismiss()
                FileDownloader.getImpl().pauseAll()
            }

            override fun warn(task: BaseDownloadTask) {
            }
        }
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = context?.getSystemService(Context.CONNECTIVITY_SERVICE)
        return if (connectivityManager is ConnectivityManager) {
            val networkInfo: NetworkInfo? = connectivityManager.activeNetworkInfo
            networkInfo?.isConnected ?: false
        } else false
    }

    private fun setToast(message: String) {
        val toast = Toast.makeText(context, message, Toast.LENGTH_LONG)
        val view: View = toast.view
        view.setBackgroundResource(R.drawable.circle_toast_background)
        val text = view.findViewById(android.R.id.message) as TextView
        text.setPadding(32, 16, 32, 16)
        context?.resources?.getColor(R.color.white)?.let { text.setTextColor(it) }
        toast.show()
    }
}