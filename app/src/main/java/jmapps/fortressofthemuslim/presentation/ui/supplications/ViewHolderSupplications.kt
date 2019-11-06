package jmapps.fortressofthemuslim.presentation.ui.supplications

import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.ToggleButton
import androidx.recyclerview.widget.RecyclerView
import jmapps.fortressofthemuslim.R

class ViewHolderSupplications(itemView: View): RecyclerView.ViewHolder(itemView) {

    val tvSupplicationArabic: TextView = itemView.findViewById(R.id.tvSupplicationArabic)
    val tvSupplicationTranscription: TextView = itemView.findViewById(R.id.tvSupplicationTranscription)
    val tvSupplicationTranslation: TextView = itemView.findViewById(R.id.tvSupplicationTranslation)
    val tvSupplicationSource: TextView = itemView.findViewById(R.id.tvSupplicationSource)

    private val btnItemShare: Button = itemView.findViewById(R.id.btnSupplicationShare)
    private val btnItemCopy: Button = itemView.findViewById(R.id.btnSupplicationCopy)
    val tbSupplicationNumber: ToggleButton = itemView.findViewById(R.id.tbSupplicationNumber)

    fun findAddRemoveFavorite(addRemoveFavorite: AdapterSupplications.AddRemoveFavorite, supplicationId: Int) {
        tbSupplicationNumber.setOnCheckedChangeListener { _, isChecked ->
            addRemoveFavorite.addRemove(isChecked, supplicationId)
        }
    }

    fun findCopy(itemCopy: AdapterSupplications.ItemCopy, content: String) {
        btnItemCopy.setOnClickListener {
            itemCopy.copy(content)
        }
    }

    fun findShare(itemShare: AdapterSupplications.ItemShare, content: String) {
        btnItemShare.setOnClickListener {
            itemShare.share(content)
        }
    }
}