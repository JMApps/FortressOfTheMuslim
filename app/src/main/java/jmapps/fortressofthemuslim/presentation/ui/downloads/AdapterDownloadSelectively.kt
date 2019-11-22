package jmapps.fortressofthemuslim.presentation.ui.downloads

import android.text.Html
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import jmapps.fortressofthemuslim.R

class AdapterDownloadSelectively(private var downloadSelectivelyList: MutableList<ModelDownloadSelectively>,
                                 private val selectChapter: SelectChapter) :
    RecyclerView.Adapter<ViewHolderDownloadSelectively>() {

    private var mainDownloadSelectivelyList: MutableList<ModelDownloadSelectively>? = null

    init {
        mainDownloadSelectivelyList = downloadSelectivelyList
    }

    interface SelectChapter {
        fun select(chapterId: Int, isChecked: Boolean)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderDownloadSelectively {
        return ViewHolderDownloadSelectively(LayoutInflater.from(parent.context).inflate(
            R.layout.item_download, parent, false))
    }

    override fun getItemCount(): Int {
        return downloadSelectivelyList.size
    }

    override fun onBindViewHolder(holder: ViewHolderDownloadSelectively, position: Int) {
        val chapterId = downloadSelectivelyList[position].chapterId
        val strChapterName = downloadSelectivelyList[position].chapterName

        holder.tvChapterDownloadName.text = Html.fromHtml(strChapterName)
        holder.cbSelectively.setOnCheckedChangeListener(null)
        holder.findCheckbox(selectChapter, chapterId!!)
    }
}