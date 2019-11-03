package jmapps.fortressofthemuslim.presentation.ui.favoriteChapters

import android.annotation.SuppressLint
import android.text.Html
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import jmapps.fortressofthemuslim.R

class AdapterFavoriteChapters(private var favoriteChapterList: MutableList<ModelFavoriteChapters>,
                              private val onItemClick: OnItemClick) :
    RecyclerView.Adapter<ViewHolderFavoriteChapter>(), Filterable {

    private var mainFavoriteChapterList: MutableList<ModelFavoriteChapters>? = null

    init {
        mainFavoriteChapterList = favoriteChapterList
    }

    interface OnItemClick {
        fun onItemClick(favoriteChapterId: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderFavoriteChapter {
        return ViewHolderFavoriteChapter(LayoutInflater.from(parent.context).inflate(
            R.layout.item_favorite_chapter, parent, false))
    }

    override fun getItemCount(): Int {
        return favoriteChapterList.size
    }

    override fun onBindViewHolder(holder: ViewHolderFavoriteChapter, position: Int) {

        val favoriteChapterId = favoriteChapterList[position].favoriteChapterId
        val strFavoriteChapterTitle = favoriteChapterList[position].strFavoriteChapterTitle

        holder.tvFavoriteChapterNumber.text = favoriteChapterId.toString()
        holder.tvFavoriteChapterTitle.text = Html.fromHtml(strFavoriteChapterTitle)

        holder.findOnItemClick(onItemClick, favoriteChapterId!!)
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            @SuppressLint("DefaultLocale")
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val charString = charSequence.toString()
                favoriteChapterList = if (charString.isEmpty()) {
                    mainFavoriteChapterList as MutableList<ModelFavoriteChapters>
                } else {
                    val filteredList = ArrayList<ModelFavoriteChapters>()
                    for (row in mainFavoriteChapterList!!) {
                        if (row.strFavoriteChapterTitle?.toLowerCase()!!.contains(charString.toLowerCase()) ||
                            row.favoriteChapterId.toString().contains(charSequence)) {
                            filteredList.add(row)
                        }
                    }
                    filteredList
                }
                val filterResults = FilterResults()
                filterResults.values = favoriteChapterList
                return filterResults
            }

            override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
                favoriteChapterList = filterResults.values as ArrayList<ModelFavoriteChapters>
                notifyDataSetChanged()
            }
        }
    }
}