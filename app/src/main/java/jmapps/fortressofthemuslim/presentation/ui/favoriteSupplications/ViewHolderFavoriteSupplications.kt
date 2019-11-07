package jmapps.fortressofthemuslim.presentation.ui.favoriteSupplications

import android.content.SharedPreferences
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.ToggleButton
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.RecyclerView
import jmapps.fortressofthemuslim.R
import jmapps.fortressofthemuslim.data.fonts.TypeFace

class ViewHolderFavoriteSupplications(itemView: View): RecyclerView.ViewHolder(itemView),
    SharedPreferences.OnSharedPreferenceChangeListener {

    private val keyArabicFont = "key_arabic_font_"
    private val keyOtherFont = "key_other_font_"

    private val keyArabicTextSize = "key_arabic_text_size"
    private val keyOtherTextSize = "key_other_text_size"

    private val keyArabicTextColor = "key_arabic_text_color"
    private val keyTranscriptionTextColor = "key_transcription_text_color"
    private val keyTranslationTextColor = "key_translation_text_color"

    private val keyTranscriptionState = "key_transcription_state"
    private val keyTranslationState = "key_translation_state"

    val tvFavoriteSupplicationArabic: TextView = itemView.findViewById(R.id.tvFavoriteSupplicationArabic)
    val tvFavoriteSupplicationTranscription: TextView = itemView.findViewById(R.id.tvFavoriteSupplicationTranscription)
    val tvFavoriteSupplicationTranslation: TextView = itemView.findViewById(R.id.tvFavoriteSupplicationTranslation)
    val tvFavoriteSupplicationSource: TextView = itemView.findViewById(R.id.tvFavoriteSupplicationSource)

    private val btnItemShare: Button = itemView.findViewById(R.id.btnSupplicationShare)
    private val btnItemCopy: Button = itemView.findViewById(R.id.btnSupplicationCopy)
    val tbFavoriteSupplicationNumber: ToggleButton = itemView.findViewById(R.id.tbFavoriteSupplicationNumber)

    private val preferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(itemView.context)
    private val textSizes = ArrayList<Float>()
    private var textColorBackgrounds: java.util.ArrayList<Int> = java.util.ArrayList()

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

        textColorBackgrounds.add(R.color.white)
        textColorBackgrounds.add(R.color.black)
        textColorBackgrounds.add(R.color.gray)
        textColorBackgrounds.add(R.color.brown)
        textColorBackgrounds.add(R.color.yellow)
        textColorBackgrounds.add(R.color.green)
        textColorBackgrounds.add(R.color.blue)
        textColorBackgrounds.add(R.color.red)
        textColorBackgrounds.add(R.color.purple)

        fontArabic()
        fontOther()
        textArabicSize()
        textOtherSize()
        textArabicColor()
        textTranscriptionColor()
        textTranslationColor()
        showTranscription()
        showTranslation()
    }

    fun findAddRemove(addRemoveFavoriteSupplication: AdapterFavoriteSupplications.AddRemoveFavoriteSupplication, favoriteSupplicationId: Int) {
        tbFavoriteSupplicationNumber.setOnCheckedChangeListener { _, isChecked ->
            addRemoveFavoriteSupplication.addRemoveSupplication(isChecked, favoriteSupplicationId)
        }
    }

    fun findCopy(itemCopy: AdapterFavoriteSupplications.ItemCopy, content: String) {
        btnItemCopy.setOnClickListener {
            itemCopy.copy(content)
        }
    }

    fun findShare(itemShare: AdapterFavoriteSupplications.ItemShare, content: String) {
        btnItemShare.setOnClickListener {
            itemShare.share(content)
        }
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        fontArabic()
        fontOther()
        textArabicSize()
        textOtherSize()
        textArabicColor()
        textTranscriptionColor()
        textTranslationColor()
        showTranscription()
        showTranslation()
    }

    private fun fontArabic() {
        val one = preferences.getBoolean("$keyArabicFont${0}", true)
        val two = preferences.getBoolean("$keyArabicFont${1}", false)
        val three = preferences.getBoolean("$keyArabicFont${2}", false)

        when(true) {
            one -> tvFavoriteSupplicationArabic.typeface = TypeFace()[itemView.context, "uthmanic.ttf"]
            two -> tvFavoriteSupplicationArabic.typeface = TypeFace()[itemView.context, "fonts/quran_font.ttf"]
            three -> tvFavoriteSupplicationArabic.typeface = TypeFace()[itemView.context, "fonts/droid_naskh.ttf"]
        }
    }

    private fun fontOther() {
        val one = preferences.getBoolean("$keyOtherFont${0}", true)
        val two = preferences.getBoolean("$keyOtherFont${1}", false)
        val three = preferences.getBoolean("$keyOtherFont${2}", false)

        when(true) {
            one -> {
                tvFavoriteSupplicationTranscription.typeface = TypeFace()[itemView.context, "fonts/gilroy_light.ttf"]
                tvFavoriteSupplicationTranslation.typeface = TypeFace()[itemView.context, "fonts/girloy_heavy.ttf"]
                tvFavoriteSupplicationSource.typeface = TypeFace()[itemView.context, "fonts/gilroy_light.ttf"]
            }
            two -> {
                tvFavoriteSupplicationTranscription.typeface = TypeFace()[itemView.context, "fonts/times_light.ttf"]
                tvFavoriteSupplicationTranslation.typeface = TypeFace()[itemView.context, "fonts/times_medium.ttf"]
                tvFavoriteSupplicationSource.typeface = TypeFace()[itemView.context, "fonts/times_light.ttf"]
            }
            three -> {
                tvFavoriteSupplicationTranscription.typeface = TypeFace()[itemView.context, "fonts/cambria_light.ttf"]
                tvFavoriteSupplicationTranslation.typeface = TypeFace()[itemView.context, "fonts/cambria_medium.ttf"]
                tvFavoriteSupplicationSource.typeface = TypeFace()[itemView.context, "fonts/cambria_light.ttf"]
            }
        }
    }

    private fun textArabicSize() {
        tvFavoriteSupplicationArabic.textSize = textSizes[preferences.getInt(keyArabicTextSize, 1)]
    }

    private fun textOtherSize() {
        tvFavoriteSupplicationTranscription.textSize = textSizes[preferences.getInt(keyOtherTextSize, 1)]
        tvFavoriteSupplicationTranslation.textSize = textSizes[preferences.getInt(keyOtherTextSize, 1)]
    }

    private fun textArabicColor() {
        tvFavoriteSupplicationArabic.setTextColor(itemView.resources.getColor(
            textColorBackgrounds[preferences.getInt(keyArabicTextColor, 2)]))
    }

    private fun textTranscriptionColor() {
        tvFavoriteSupplicationTranscription.setTextColor(
            itemView.resources.getColor(textColorBackgrounds[preferences.getInt(keyTranscriptionTextColor, 2)]))
        tvFavoriteSupplicationSource.setTextColor(
            itemView.resources.getColor(textColorBackgrounds[preferences.getInt(keyTranscriptionTextColor, 2)]))
    }

    private fun textTranslationColor() {
        tvFavoriteSupplicationTranslation.setTextColor(itemView.resources.getColor(
            textColorBackgrounds[preferences.getInt(keyTranslationTextColor, 2)]))
    }

    private fun showTranscription() {
        if (preferences.getBoolean(keyTranscriptionState, true)) {
            tvFavoriteSupplicationTranscription.visibility = View.VISIBLE
        } else {
            tvFavoriteSupplicationTranscription.visibility = View.GONE
        }
    }

    private fun showTranslation() {
        if (preferences.getBoolean(keyTranslationState, true)) {
            tvFavoriteSupplicationTranslation.visibility = View.VISIBLE
        } else {
            tvFavoriteSupplicationTranslation.visibility = View.GONE
        }
    }
}