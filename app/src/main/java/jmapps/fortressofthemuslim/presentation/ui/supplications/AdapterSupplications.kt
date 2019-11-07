package jmapps.fortressofthemuslim.presentation.ui.supplications

import android.content.SharedPreferences
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import jmapps.fortressofthemuslim.R

class AdapterSupplications(
    private var supplicationList: MutableList<ModelSupplications>,
    private val addRemoveFavoriteSupplication: AddRemoveFavoriteSupplication,
    private val preferences: SharedPreferences,
    private val itemCopy: ItemCopy,
    private val itemShare: ItemShare) :
    RecyclerView.Adapter<ViewHolderSupplications>() {

    interface AddRemoveFavoriteSupplication {
        fun addRemoveSupplication(state: Boolean, supplicationId: Int)
    }

    interface ItemCopy {
        fun copy(content: String)
    }

    interface ItemShare {
        fun share(content: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderSupplications {
        return ViewHolderSupplications(LayoutInflater.from(parent.context).inflate(
                R.layout.item_supplication, parent, false))
    }

    override fun getItemCount(): Int {
        return supplicationList.size
    }

    override fun onBindViewHolder(holder: ViewHolderSupplications, position: Int) {
        val supplicationId = supplicationList[position].supplicationId
        val strSupplicationArabic = supplicationList[position].strSupplicationArabic
        val strSupplicationTranscription = supplicationList[position].strSupplicationTranscription
        val strSupplicationTranslation = supplicationList[position].strSupplicationTranslation
        val strSupplicationSource = supplicationList[position].strSupplicationSource

        if (!strSupplicationArabic.isNullOrEmpty() && holder.tvSupplicationArabic.isVisible) {
            holder.tvSupplicationArabic.visibility = View.VISIBLE
            holder.tvSupplicationArabic.text = Html.fromHtml(strSupplicationArabic)
        } else {
            holder.tvSupplicationArabic.visibility = View.GONE
        }

        if (!strSupplicationTranscription.isNullOrEmpty() && holder.tvSupplicationTranscription.isVisible) {
            holder.tvSupplicationTranscription.visibility = View.VISIBLE
            holder.tvSupplicationTranscription.text = strSupplicationTranscription
        } else {
            holder.tvSupplicationTranscription.visibility = View.GONE
        }

        if (!strSupplicationTranslation.isNullOrEmpty() && holder.tvSupplicationTranslation.isVisible) {
            holder.tvSupplicationTranslation.visibility = View.VISIBLE
            holder.tvSupplicationTranslation.text = Html.fromHtml(strSupplicationTranslation)
        } else {
            holder.tvSupplicationTranslation.visibility = View.GONE
        }

        if (!strSupplicationSource.isNullOrEmpty() && holder.tvSupplicationSource.isVisible) {
            holder.tvSupplicationSource.visibility = View.VISIBLE
            holder.tvSupplicationSource.text = strSupplicationSource
        } else {
            holder.tvSupplicationSource.visibility = View.GONE
        }

        holder.tbSupplicationNumber.setOnCheckedChangeListener(null)
        holder.tbSupplicationNumber.isChecked = preferences.getBoolean(
            "key_item_bookmark_$supplicationId", false)
        holder.tbSupplicationNumber.text = supplicationId.toString()
        holder.tbSupplicationNumber.textOn = supplicationId.toString()
        holder.tbSupplicationNumber.textOff = supplicationId.toString()

        val content: String? =
            "${strSupplicationArabic.orEmpty()}<p/>${strSupplicationTranscription.orEmpty()}<p/>" +
                    "${strSupplicationTranslation.orEmpty()}<p/>${strSupplicationSource.orEmpty()}"

        holder.findAddRemoveFavorite(addRemoveFavoriteSupplication, supplicationId!!)
        holder.findCopy(itemCopy, content!!)
        holder.findShare(itemShare, content)
    }
}