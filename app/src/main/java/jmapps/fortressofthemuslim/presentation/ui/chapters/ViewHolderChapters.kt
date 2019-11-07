package jmapps.fortressofthemuslim.presentation.ui.chapters

import android.view.View
import android.widget.TextView
import android.widget.ToggleButton
import androidx.recyclerview.widget.RecyclerView
import jmapps.fortressofthemuslim.R

class ViewHolderChapters(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val tbChapterNumber: ToggleButton = itemView.findViewById(R.id.tbChapterNumber)
    val tvChapterTitle: TextView = itemView.findViewById(R.id.tvChapterTitle)

    fun findAddRemoveFavorite(addRemoveFavoriteChapter: AdapterChapters.AddRemoveFavoriteChapter, idChapter: Int) {
        tbChapterNumber.setOnCheckedChangeListener { _, isChecked ->
            addRemoveFavoriteChapter.addRemoveChapter(isChecked, idChapter)
        }
    }

    fun findOnItemClick(onItemClick: AdapterChapters.OnItemClick, chapterId: Int) {
        itemView.setOnClickListener {
            onItemClick.onItemClick(chapterId)
        }
    }
}