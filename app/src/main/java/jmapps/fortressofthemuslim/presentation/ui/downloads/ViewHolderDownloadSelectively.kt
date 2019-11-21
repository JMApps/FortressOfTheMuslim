package jmapps.fortressofthemuslim.presentation.ui.downloads

import android.view.View
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import jmapps.fortressofthemuslim.R

class ViewHolderDownloadSelectively(itemView: View): RecyclerView.ViewHolder(itemView) {

    val cbSelectively: CheckBox = itemView.findViewById(R.id.cbSelectively)
    val tvChapterDownloadName: TextView = itemView.findViewById(R.id.tvChapterDownloadName)

    fun findCheckbox(selectChapter: AdapterDownloadSelectively.SelectChapter, chapterId: Int) {
        cbSelectively.setOnCheckedChangeListener { _, isChecked ->
            selectChapter.select(chapterId, isChecked)
        }
    }
}