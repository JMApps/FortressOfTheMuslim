package jmapps.fortressofthemuslim.presentation.ui.favoriteSupplications

import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.ToggleButton
import androidx.recyclerview.widget.RecyclerView
import jmapps.fortressofthemuslim.R

class ViewHolderFavoriteSupplications(itemView: View): RecyclerView.ViewHolder(itemView) {

    val tvFavoriteSupplicationArabic: TextView = itemView.findViewById(R.id.tvFavoriteSupplicationArabic)
    val tvFavoriteSupplicationTranscription: TextView = itemView.findViewById(R.id.tvFavoriteSupplicationTranscription)
    val tvFavoriteSupplicationTranslation: TextView = itemView.findViewById(R.id.tvFavoriteSupplicationTranslation)
    val tvFavoriteSupplicationSource: TextView = itemView.findViewById(R.id.tvFavoriteSupplicationSource)

    val btnItemShare: Button = itemView.findViewById(R.id.btnSupplicationShare)
    val btnItemCopy: Button = itemView.findViewById(R.id.btnSupplicationCopy)
    val tbFavoriteSupplicationNumber: ToggleButton = itemView.findViewById(R.id.tbFavoriteSupplicationNumber)

    fun findAddRemove(addRemoveFavorite: AdapterFavoriteSupplications.AddRemoveFavorite, favoriteSupplicationId: Int) {
        tbFavoriteSupplicationNumber.setOnCheckedChangeListener { _, isChecked ->
            addRemoveFavorite.addRemove(isChecked, favoriteSupplicationId)
        }
    }
}