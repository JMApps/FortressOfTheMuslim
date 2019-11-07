package jmapps.fortressofthemuslim.presentation.ui.chapters

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.text.Html
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import jmapps.fortressofthemuslim.R

class AdapterChapters(private var chapterList: MutableList<ModelChapters>,
                      private val addRemoveFavoriteChapter: AddRemoveFavoriteChapter,
                      private val preferences: SharedPreferences,
                      private val onItemClick: OnItemClick) :
    RecyclerView.Adapter<ViewHolderChapters>(), Filterable {

    private var mainChapterList: MutableList<ModelChapters>? = null

    init {
        mainChapterList = chapterList
    }

    interface AddRemoveFavoriteChapter {
        fun addRemoveChapter(state: Boolean, chapterId: Int)
    }

    interface OnItemClick {
        fun onItemClick(chapterId: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderChapters {
        return ViewHolderChapters(LayoutInflater.from(parent.context).inflate(
                R.layout.item_chapter, parent, false))
    }

    override fun getItemCount(): Int {
        return chapterList.size
    }

    override fun onBindViewHolder(holder: ViewHolderChapters, position: Int) {
        val chapterId = chapterList[position].chapterId
        val strChapterTitle = chapterList[position].strChapterTitle

        holder.tbChapterNumber.setOnCheckedChangeListener(null)
        holder.tbChapterNumber.isChecked = preferences.getBoolean(
            "key_chapter_bookmark_$chapterId", false)
        holder.tbChapterNumber.text = chapterId.toString()
        holder.tbChapterNumber.textOn = chapterId.toString()
        holder.tbChapterNumber.textOff = chapterId.toString()
        holder.tvChapterTitle.text = Html.fromHtml(strChapterTitle)

        holder.findAddRemoveFavorite(addRemoveFavoriteChapter, chapterId!!)
        holder.findOnItemClick(onItemClick, chapterId)
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            @SuppressLint("DefaultLocale")
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val charString = charSequence.toString()
                chapterList = if (charString.isEmpty()) {
                    mainChapterList as MutableList<ModelChapters>
                } else {
                    val filteredList = ArrayList<ModelChapters>()
                    for (row in mainChapterList!!) {
                        if (row.strChapterTitle?.toLowerCase()!!.contains(charString.toLowerCase()) ||
                            row.chapterId.toString().contains(charSequence)) {
                            filteredList.add(row)
                        }
                    }
                    filteredList
                }
                val filterResults = FilterResults()
                filterResults.values = chapterList
                return filterResults
            }

            override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
                chapterList = filterResults.values as ArrayList<ModelChapters>
                notifyDataSetChanged()
            }
        }
    }
}