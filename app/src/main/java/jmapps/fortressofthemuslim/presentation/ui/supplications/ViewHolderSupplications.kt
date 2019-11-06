package jmapps.fortressofthemuslim.presentation.ui.supplications

import android.content.SharedPreferences
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.ToggleButton
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.RecyclerView
import jmapps.fortressofthemuslim.R
import jmapps.fortressofthemuslim.data.fonts.TypeFace

class ViewHolderSupplications(itemView: View): RecyclerView.ViewHolder(itemView),
    SharedPreferences.OnSharedPreferenceChangeListener {

    private val keyArabicFont = "key_arabic_font_"
    private val keyOtherFont = "key_other_font_"

    private val keyArabicTextSize = "key_arabic_text_size"
    private val keyOtherTextSize = "key_other_text_size"

    private val keyTranscriptionState = "key_transcription_state"
    private val keyTranslationState = "key_translation_state"

    val tvSupplicationArabic: TextView = itemView.findViewById(R.id.tvSupplicationArabic)
    val tvSupplicationTranscription: TextView = itemView.findViewById(R.id.tvSupplicationTranscription)
    val tvSupplicationTranslation: TextView = itemView.findViewById(R.id.tvSupplicationTranslation)
    val tvSupplicationSource: TextView = itemView.findViewById(R.id.tvSupplicationSource)

    private val preferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(itemView.context)
    private val textSizes = ArrayList<Float>()

    init {
        PreferenceManager.getDefaultSharedPreferences(itemView.context).registerOnSharedPreferenceChangeListener(this)

        textSizes.add(16f)
        textSizes.add(18f)
        textSizes.add(20f)
        textSizes.add(22f)
        textSizes.add(24f)
        textSizes.add(26f)
        textSizes.add(28f)
        textSizes.add(30f)

        fontArabic()
        fontOther()
        textArabicSize()
        textOtherSize()
        showTranscription()
        showTranslation()
    }

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

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        fontArabic()
        fontOther()
        textArabicSize()
        textOtherSize()
        showTranscription()
        showTranslation()
    }

    private fun fontArabic() {
        val one = preferences.getBoolean("$keyArabicFont${0}", true)
        val two = preferences.getBoolean("$keyArabicFont${1}", false)
        val three = preferences.getBoolean("$keyArabicFont${2}", false)

        when(true) {
            one -> tvSupplicationArabic.typeface = TypeFace()[itemView.context, "uthmanic.ttf"]
            two -> tvSupplicationArabic.typeface = TypeFace()[itemView.context, "fonts/quran_font.ttf"]
            three -> tvSupplicationArabic.typeface = TypeFace()[itemView.context, "fonts/droid_naskh.ttf"]
        }
    }

    private fun fontOther() {
        val one = preferences.getBoolean("$keyOtherFont${0}", true)
        val two = preferences.getBoolean("$keyOtherFont${1}", false)
        val three = preferences.getBoolean("$keyOtherFont${2}", false)

        when(true) {
            one -> {
                tvSupplicationTranscription.typeface = TypeFace()[itemView.context, "fonts/gilroy_light.ttf"]
                tvSupplicationTranslation.typeface = TypeFace()[itemView.context, "fonts/girloy_heavy.ttf"]
                tvSupplicationSource.typeface = TypeFace()[itemView.context, "fonts/gilroy_light.ttf"]
            }
            two -> {
                tvSupplicationTranscription.typeface = TypeFace()[itemView.context, "fonts/times_light.ttf"]
                tvSupplicationTranslation.typeface = TypeFace()[itemView.context, "fonts/times_medium.ttf"]
                tvSupplicationSource.typeface = TypeFace()[itemView.context, "fonts/times_light.ttf"]
            }
            three -> {
                tvSupplicationTranscription.typeface = TypeFace()[itemView.context, "fonts/cambria_light.ttf"]
                tvSupplicationTranslation.typeface = TypeFace()[itemView.context, "fonts/cambria_medium.ttf"]
                tvSupplicationSource.typeface = TypeFace()[itemView.context, "fonts/cambria_light.ttf"]
            }
        }
    }

    private fun textArabicSize() {
        tvSupplicationArabic.textSize = textSizes[preferences.getInt(keyArabicTextSize, 1)]
    }

    private fun textOtherSize() {
        tvSupplicationTranscription.textSize = textSizes[preferences.getInt(keyOtherTextSize, 1)]
        tvSupplicationTranslation.textSize = textSizes[preferences.getInt(keyOtherTextSize, 1)]
    }

    private fun showTranscription() {
        if (preferences.getBoolean(keyTranscriptionState, true)) {
            tvSupplicationTranscription.visibility = View.VISIBLE
        } else {
            tvSupplicationTranscription.visibility = View.GONE
        }
    }

    private fun showTranslation() {
        if (preferences.getBoolean(keyTranslationState, true)) {
            tvSupplicationTranslation.visibility = View.VISIBLE
        } else {
            tvSupplicationTranslation.visibility = View.GONE
        }
    }
}