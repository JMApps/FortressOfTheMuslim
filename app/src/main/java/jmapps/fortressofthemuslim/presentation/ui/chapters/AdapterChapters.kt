package jmapps.fortressofthemuslim.presentation.ui.chapters

import android.text.Html
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import jmapps.fortressofthemuslim.R

class AdapterChapters(private val chapterList: MutableList<ModelChapters>):
    RecyclerView.Adapter<ViewHolderChapters>() {

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

        holder.tvChapterNumber.text = chapterId.toString()
        holder.tvChapterTitle.text = Html.fromHtml(strChapterTitle)
    }
}