package jmapps.fortressofthemuslim.presentation.ui.favoriteSupplications

import android.content.SharedPreferences
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import jmapps.fortressofthemuslim.R

data class AdapterFavoriteSupplications(private val favoriteSupplicationList: MutableList<ModelFavoriteSupplications>,
                                        private val addRemoveFavorite: AddRemoveFavorite,
                                        private val preferences: SharedPreferences):
        RecyclerView.Adapter<ViewHolderFavoriteSupplications>() {

    interface AddRemoveFavorite {
        fun addRemove(state: Boolean, favoriteSupplicationId: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderFavoriteSupplications {
        return ViewHolderFavoriteSupplications(LayoutInflater.from(parent.context).inflate(
            R.layout.item_favorite_supplication, parent, false))
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
            holder.tvFavoriteSupplicationArabic.text = Html.fromHtml(strFavoriteSupplicationArabic)
        } else {
            holder.tvFavoriteSupplicationArabic.visibility = View.GONE
        }

        if (!strFavoriteSupplicationTranscription.isNullOrEmpty()) {
            holder.tvFavoriteSupplicationTranscription.text = strFavoriteSupplicationTranscription
        } else {
            holder.tvFavoriteSupplicationTranscription.visibility = View.GONE
        }

        if (!strFavoriteSupplicationTranslation.isNullOrEmpty()) {
            holder.tvFavoriteSupplicationTranslation.text = Html.fromHtml(strFavoriteSupplicationTranslation)
        } else {
            holder.tvFavoriteSupplicationTranslation.visibility = View.GONE
        }

        if (!strFavoriteSupplicationSource.isNullOrEmpty()) {
            holder.tvFavoriteSupplicationSource.text = strFavoriteSupplicationSource
        } else {
            holder.tvFavoriteSupplicationSource.visibility = View.GONE
        }

        holder.tbFavoriteSupplicationNumber.setOnCheckedChangeListener(null)
        holder.tbFavoriteSupplicationNumber.isChecked = preferences.getBoolean(
            "key_item_bookmark_§$favoriteSupplicationId", false)
        holder.tbFavoriteSupplicationNumber.text = favoriteSupplicationId.toString()
        holder.tbFavoriteSupplicationNumber.textOn = favoriteSupplicationId.toString()
        holder.tbFavoriteSupplicationNumber.textOff = favoriteSupplicationId.toString()

        holder.findAddRemove(addRemoveFavorite, favoriteSupplicationId!!)
    }
}