package jmapps.fortressofthemuslim.presentation.ui.favoriteSupplications

import android.text.Html
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import jmapps.fortressofthemuslim.R

data class AdapterFavoriteSupplications(private val favoriteSupplicationList: MutableList<ModelFavoriteSupplications>):
        RecyclerView.Adapter<ViewHolderFavoriteSupplications>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderFavoriteSupplications {
        return ViewHolderFavoriteSupplications(LayoutInflater.from(parent.context).inflate(
            R.layout.item_favorite_supplication, parent, false))
    }

    override fun getItemCount(): Int {
        return favoriteSupplicationList.size
    }

    override fun onBindViewHolder(holder: ViewHolderFavoriteSupplications, position: Int) {
        val supplicationId = favoriteSupplicationList[position].favoriteSupplicationId
        val strSupplicationArabic = favoriteSupplicationList[position].strFavoriteSupplicationArabic
        val strSupplicationTranscription = favoriteSupplicationList[position].strFavoriteSupplicationTranscription
        val strSupplicationTranslation = favoriteSupplicationList[position].strFavoriteSupplicationTranslation
        val strSupplicationSource = favoriteSupplicationList[position].strFavoriteSupplicationSource

        holder.tvFavoriteSupplicationArabic.text = Html.fromHtml(strSupplicationArabic)
        holder.tvFavoriteSupplicationTranscription.text = strSupplicationTranscription
        holder.tvFavoriteSupplicationTranslation.text = Html.fromHtml(strSupplicationTranslation)
        holder.tvFavoriteSupplicationSource.text = strSupplicationSource
        holder.tbFavoriteSupplicationNumber.setOnCheckedChangeListener(null)
        holder.tbFavoriteSupplicationNumber.text = supplicationId.toString()
        holder.tbFavoriteSupplicationNumber.textOn = supplicationId.toString()
        holder.tbFavoriteSupplicationNumber.textOff = supplicationId.toString()
    }
}