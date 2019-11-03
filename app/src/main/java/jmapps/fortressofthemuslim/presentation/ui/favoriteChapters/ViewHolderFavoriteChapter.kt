package jmapps.fortressofthemuslim.presentation.ui.favoriteChapters

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import jmapps.fortressofthemuslim.R

class ViewHolderFavoriteChapter(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val tvFavoriteChapterNumber: TextView = itemView.findViewById(R.id.tvFavoriteChapterNumber)
    val tvFavoriteChapterTitle: TextView = itemView.findViewById(R.id.tvFavoriteChapterTitle)

    fun findOnItemClick(onItemClick: AdapterFavoriteChapters.OnItemClick, favoriteChapterId: Int) {
        itemView.setOnClickListener {
            onItemClick.onItemClick(favoriteChapterId)
        }
    }
}