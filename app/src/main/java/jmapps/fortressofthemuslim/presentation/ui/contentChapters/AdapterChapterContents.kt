package jmapps.fortressofthemuslim.presentation.ui.contentChapters

import android.content.SharedPreferences
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import jmapps.fortressofthemuslim.R

class AdapterChapterContents(
    private var chapterContent: MutableList<ModelChapterContents>,
    private val addRemoveFavorite: AddRemoveFavorite,
    private val preferences: SharedPreferences,
    private val itemCopy: ItemCopy,
    private val itemShare: ItemShare) :
    RecyclerView.Adapter<ViewHolderChapterContents>() {

    interface AddRemoveFavorite {
        fun addRemove(state: Boolean, chapterContentId: Int)
    }

    interface ItemCopy {
        fun copy(content: String)
    }

    interface ItemShare {
        fun share(content: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderChapterContents {
        return ViewHolderChapterContents(LayoutInflater.from(parent.context).inflate(
                R.layout.item_chapter_content, parent, false))
    }

    override fun getItemCount(): Int {
        return chapterContent.size
    }

    override fun onBindViewHolder(holder: ViewHolderChapterContents, position: Int) {
        val contentChapterId = chapterContent[position].chapterContentId
        val strChapterContentArabic = chapterContent[position].strChapterContentArabic
        val strChapterContentTranscription = chapterContent[position].strChapterContentTranscription
        val strChapterContentTranslation = chapterContent[position].strChapterContentTranslation
        val strChapterContentSource = chapterContent[position].strChapterContentSource

        if (!strChapterContentArabic.isNullOrEmpty() && holder.tvChapterContentArabic.isVisible) {
            holder.tvChapterContentArabic.visibility = View.VISIBLE
            holder.tvChapterContentArabic.text = Html.fromHtml(strChapterContentArabic)
        } else {
            holder.tvChapterContentArabic.visibility = View.GONE
        }

        if (!strChapterContentTranscription.isNullOrEmpty() && holder.tvChapterContentTranscription.isVisible) {
            holder.tvChapterContentTranscription.visibility = View.VISIBLE
            holder.tvChapterContentTranscription.text = strChapterContentTranscription
        } else {
            holder.tvChapterContentTranscription.visibility = View.GONE
        }

        if (!strChapterContentTranslation.isNullOrEmpty() && holder.tvChapterContentTranslation.isVisible) {
            holder.tvChapterContentTranslation.visibility = View.VISIBLE
            holder.tvChapterContentTranslation.text = Html.fromHtml(strChapterContentTranslation)
        } else {
            holder.tvChapterContentTranslation.visibility = View.GONE
        }

        if (!strChapterContentSource.isNullOrEmpty() && holder.tvChapterContentSource.isVisible) {
            holder.tvChapterContentSource.visibility = View.VISIBLE
            holder.tvChapterContentSource.text = strChapterContentSource
        } else {
            holder.tvChapterContentSource.visibility = View.GONE
        }

        holder.tbChapterContentNumber.setOnCheckedChangeListener(null)
        holder.tbChapterContentNumber.isChecked = preferences.getBoolean(
            "key_item_bookmark_$contentChapterId", false)
        holder.tbChapterContentNumber.text = contentChapterId.toString()
        holder.tbChapterContentNumber.textOn = contentChapterId.toString()
        holder.tbChapterContentNumber.textOff = contentChapterId.toString()

        val content: String? =
            "${strChapterContentArabic.orEmpty()}<p/>${strChapterContentTranscription.orEmpty()}<p/>" +
                    "${strChapterContentTranslation.orEmpty()}<p/>${strChapterContentSource.orEmpty()}"

        holder.findAddRemoveFavorite(addRemoveFavorite, contentChapterId!!)
        holder.findCopy(itemCopy, content!!)
        holder.findShare(itemShare, content)
    }
}