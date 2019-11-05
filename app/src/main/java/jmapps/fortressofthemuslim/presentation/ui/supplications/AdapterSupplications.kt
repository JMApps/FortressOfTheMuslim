package jmapps.fortressofthemuslim.presentation.ui.supplications

import android.text.Html
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import jmapps.fortressofthemuslim.R

class AdapterSupplications(private var supplicationList: MutableList<ModelSupplications>) :
    RecyclerView.Adapter<ViewHolderSupplications>() {

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

        holder.tvSupplicationArabic.text = Html.fromHtml(strSupplicationArabic)
        holder.tvSupplicationTranscription.text = strSupplicationTranscription
        holder.tvSupplicationTranslation.text = Html.fromHtml(strSupplicationTranslation)
        holder.tvSupplicationSource.text = strSupplicationSource
        holder.tbSupplicationNumber.setOnCheckedChangeListener(null)
        holder.tbSupplicationNumber.text = supplicationId.toString()
        holder.tbSupplicationNumber.textOn = supplicationId.toString()
        holder.tbSupplicationNumber.textOff = supplicationId.toString()
    }
}