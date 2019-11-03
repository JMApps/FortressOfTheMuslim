package jmapps.fortressofthemuslim.presentation.ui.chapters

import android.view.View
import android.widget.TextView
import android.widget.ToggleButton
import androidx.recyclerview.widget.RecyclerView
import jmapps.fortressofthemuslim.R

class ViewHolderChapters(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val tvChapterNumber: TextView = itemView.findViewById(R.id.tvChapterNumber)
    val tbFavoriteChapter: ToggleButton = itemView.findViewById(R.id.tbFavoriteChapter)
    val tvChapterTitle: TextView = itemView.findViewById(R.id.tvChapterTitle)
}