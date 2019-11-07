package jmapps.fortressofthemuslim.presentation.ui.favoriteChapters

import android.view.View
import android.widget.TextView
import android.widget.ToggleButton
import androidx.recyclerview.widget.RecyclerView
import jmapps.fortressofthemuslim.R
import jmapps.fortressofthemuslim.presentation.ui.chapters.AdapterChapters

class ViewHolderFavoriteChapters(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val tbFavoriteChapterNumber: ToggleButton = itemView.findViewById(R.id.tbFavoriteChapterNumber)
    val tvFavoriteChapterTitle: TextView = itemView.findViewById(R.id.tvFavoriteChapterTitle)

    fun findAddRemoveFavorite(addRemoveFavoriteChapter: AdapterFavoriteChapters.AddRemoveFavoriteChapter, idFavoriteChapter: Int) {
        tbFavoriteChapterNumber.setOnCheckedChangeListener { _, isChecked ->
            addRemoveFavoriteChapter.addRemoveChapter(isChecked, idFavoriteChapter)
        }
    }

    fun findOnItemClick(onItemClick: AdapterFavoriteChapters.OnItemClick, favoriteChapterId: Int) {
        itemView.setOnClickListener {
            onItemClick.onItemClick(favoriteChapterId)
        }
    }
}