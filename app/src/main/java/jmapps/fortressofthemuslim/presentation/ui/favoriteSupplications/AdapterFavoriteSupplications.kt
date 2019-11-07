package jmapps.fortressofthemuslim.presentation.ui.favoriteSupplications

import android.content.SharedPreferences
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import jmapps.fortressofthemuslim.R
import jmapps.fortressofthemuslim.presentation.ui.supplications.AdapterSupplications

data class AdapterFavoriteSupplications(
    private val favoriteSupplicationList: MutableList<ModelFavoriteSupplications>,
    private val addRemoveFavoriteSupplication: AddRemoveFavoriteSupplication,
    private val preferences: SharedPreferences,
    private val itemCopy: ItemCopy,
    private val itemShare: ItemShare) :
    RecyclerView.Adapter<ViewHolderFavoriteSupplications>() {

    interface AddRemoveFavoriteSupplication {
        fun addRemoveSupplication(state: Boolean, favoriteSupplicationId: Int)
    }

    interface ItemCopy {
        fun copy(content: String)
    }

    interface ItemShare {
        fun share(content: String)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolderFavoriteSupplications {
        return ViewHolderFavoriteSupplications(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_favorite_supplication, parent, false
            )
        )
    }

    override fun getItemCount(): Int {
        return favoriteSupplicationList.size
    }

    override fun onBindViewHolder(holder: ViewHolderFavoriteSupplications, position: Int) {
        val favoriteSupplicationId = favoriteSupplicationList[position].favoriteSupplicationId
        val strFavoriteSupplicationArabic = favoriteSupplicationList[position].strFavoriteSupplicationArabic
        val strFavoriteSupplicationTranscription = favoriteSupplicationList[position].strFavoriteSupplicationTranscription
        val strFavoriteSupplicationTranslation = favoriteSupplicationList[position].strFavoriteSupplicationTranslation
        val strFavoriteSupplicationSource = favoriteSupplicationList[position].strFavoriteSupplicationSource

        if (!strFavoriteSupplicationArabic.isNullOrEmpty()) {
            holder.tvFavoriteSupplicationArabic.visibility = View.VISIBLE
            holder.tvFavoriteSupplicationArabic.text = Html.fromHtml(strFavoriteSupplicationArabic)
        } else {
            holder.tvFavoriteSupplicationArabic.visibility = View.GONE
        }

        if (!strFavoriteSupplicationTranscription.isNullOrEmpty()) {
            holder.tvFavoriteSupplicationTranscription.visibility = View.VISIBLE
            holder.tvFavoriteSupplicationTranscription.text = strFavoriteSupplicationTranscription
        } else {
            holder.tvFavoriteSupplicationTranscription.visibility = View.GONE
        }

        if (!strFavoriteSupplicationTranslation.isNullOrEmpty()) {
            holder.tvFavoriteSupplicationTranslation.visibility = View.VISIBLE
            holder.tvFavoriteSupplicationTranslation.text = Html.fromHtml(strFavoriteSupplicationTranslation)
        } else {
            holder.tvFavoriteSupplicationTranslation.visibility = View.GONE
        }

        if (!strFavoriteSupplicationSource.isNullOrEmpty()) {
            holder.tvFavoriteSupplicationSource.visibility = View.VISIBLE
            holder.tvFavoriteSupplicationSource.text = strFavoriteSupplicationSource
        } else {
            holder.tvFavoriteSupplicationSource.visibility = View.GONE
        }

        holder.tbFavoriteSupplicationNumber.setOnCheckedChangeListener(null)
        holder.tbFavoriteSupplicationNumber.isChecked = preferences.getBoolean(
            "key_item_bookmark_$favoriteSupplicationId", false
        )
        holder.tbFavoriteSupplicationNumber.text = favoriteSupplicationId.toString()
        holder.tbFavoriteSupplicationNumber.textOn = favoriteSupplicationId.toString()
        holder.tbFavoriteSupplicationNumber.textOff = favoriteSupplicationId.toString()

        val content: String? =
            "${strFavoriteSupplicationArabic.orEmpty()}<p/>${strFavoriteSupplicationTranscription.orEmpty()}<p/>" +
                    "${strFavoriteSupplicationTranslation.orEmpty()}<p/>${strFavoriteSupplicationSource.orEmpty()}"

        holder.findAddRemove(addRemoveFavoriteSupplication, favoriteSupplicationId!!)
        holder.findCopy(itemCopy, content!!)
        holder.findShare(itemShare, content)
    }
}