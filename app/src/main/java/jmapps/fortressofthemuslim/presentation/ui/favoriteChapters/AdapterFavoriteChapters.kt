package jmapps.fortressofthemuslim.presentation.ui.favoriteChapters

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.text.Html
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import jmapps.fortressofthemuslim.R

class AdapterFavoriteChapters(
    private var favoriteChapterList: MutableList<ModelFavoriteChapters>,
    private val addRemoveFavoriteChapter: AddRemoveFavoriteChapter,
    private val preferences: SharedPreferences,
    private val onItemClick: OnItemClick) :
    RecyclerView.Adapter<ViewHolderFavoriteChapters>(), Filterable {

    private var mainFavoriteChapterList: MutableList<ModelFavoriteChapters>? = null

    init {
        mainFavoriteChapterList = favoriteChapterList
    }

    interface AddRemoveFavoriteChapter {
        fun addRemoveChapter(state: Boolean, favoriteChapterId: Int)
    }

    interface OnItemClick {
        fun onItemClick(favoriteChapterId: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderFavoriteChapters {
        return ViewHolderFavoriteChapters(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_favorite_chapter, parent, false
            )
        )
    }

    override fun getItemCount(): Int {
        return favoriteChapterList.size
    }

    override fun onBindViewHolder(holder: ViewHolderFavoriteChapters, position: Int) {

        val favoriteChapterId = favoriteChapterList[position].favoriteChapterId
        val strFavoriteChapterTitle = favoriteChapterList[position].strFavoriteChapterTitle

        holder.tbFavoriteChapterNumber.setOnCheckedChangeListener(null)
        holder.tbFavoriteChapterNumber.isChecked = preferences.getBoolean(
            "key_chapter_bookmark_$favoriteChapterId", false
        )
        holder.tbFavoriteChapterNumber.text = favoriteChapterId.toString()
        holder.tbFavoriteChapterNumber.textOn = favoriteChapterId.toString()
        holder.tbFavoriteChapterNumber.textOff = favoriteChapterId.toString()
        holder.tvFavoriteChapterTitle.text = Html.fromHtml(strFavoriteChapterTitle)

        holder.findAddRemoveFavorite(addRemoveFavoriteChapter, favoriteChapterId!!)
        holder.findOnItemClick(onItemClick, favoriteChapterId)
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
                            row.favoriteChapterId.toString().contains(charSequence)
                        ) {
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