package jmapps.fortressofthemuslim.presentation.ui.downloads

import android.annotation.SuppressLint
import android.text.Html
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import jmapps.fortressofthemuslim.R

class AdapterDownloadSelectively(private var downloadSelectivelyList: MutableList<ModelDownloadSelectively>,
                                 private val selectChapter: SelectChapter) :
    RecyclerView.Adapter<ViewHolderDownloadSelectively>(), Filterable {

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

    override fun getFilter(): Filter {
        return object : Filter() {
            @SuppressLint("DefaultLocale")
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val charString = charSequence.toString()
                downloadSelectivelyList = if (charString.isEmpty()) {
                    mainDownloadSelectivelyList as MutableList<ModelDownloadSelectively>
                } else {
                    val filteredList = ArrayList<ModelDownloadSelectively>()
                    for (row in mainDownloadSelectivelyList!!) {
                        if (row.chapterName?.toLowerCase()!!.contains(charString.toLowerCase()) ||
                            row.chapterId.toString().contains(charSequence)
                        ) {
                            filteredList.add(row)
                        }
                    }
                    filteredList
                }
                val filterResults = FilterResults()
                filterResults.values = downloadSelectivelyList
                return filterResults
            }

            override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
                downloadSelectivelyList = filterResults.values as ArrayList<ModelDownloadSelectively>
                notifyDataSetChanged()
            }
        }
    }
}