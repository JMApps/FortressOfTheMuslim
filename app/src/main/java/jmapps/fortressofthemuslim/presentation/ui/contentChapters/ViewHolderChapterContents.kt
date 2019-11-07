package jmapps.fortressofthemuslim.presentation.ui.contentChapters

import android.content.SharedPreferences
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.ToggleButton
import androidx.cardview.widget.CardView
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.RecyclerView
import jmapps.fortressofthemuslim.R
import jmapps.fortressofthemuslim.data.fonts.TypeFace

class ViewHolderChapterContents(itemView: View): RecyclerView.ViewHolder(itemView),
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

    val tvChapterContentArabic: TextView = itemView.findViewById(R.id.tvChapterContentArabic)
    val tvChapterContentTranscription: TextView = itemView.findViewById(R.id.tvChapterContentTranscription)
    val tvChapterContentTranslation: TextView = itemView.findViewById(R.id.tvChapterContentTranslation)
    val tvChapterContentSource: TextView = itemView.findViewById(R.id.tvChapterContentSource)

    private val btnItemShare: Button = itemView.findViewById(R.id.btnChapterContentShare)
    private val btnItemCopy: Button = itemView.findViewById(R.id.btnChapterContentCopy)
    val tbChapterContentNumber: ToggleButton = itemView.findViewById(R.id.tbChapterContentNumber)

    private val preferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(itemView.context)
    private val textSizes = ArrayList<Float>()
    private var textColorBackgrounds: ArrayList<Int> = ArrayList()

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

    fun findAddRemoveFavorite(addRemoveFavoriteSupplication: AdapterChapterContents.AddRemoveFavoriteSupplication, supplicationId: Int) {
        tbChapterContentNumber.setOnCheckedChangeListener { _, isChecked ->
            addRemoveFavoriteSupplication.addRemoveSupplication(isChecked, supplicationId)
        }
    }

    fun findCopy(itemCopy: AdapterChapterContents.ItemCopy, content: String) {
        btnItemCopy.setOnClickListener {
            itemCopy.copy(content)
        }
    }

    fun findShare(itemShare: AdapterChapterContents.ItemShare, content: String) {
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
            one -> tvChapterContentArabic.typeface = TypeFace()[itemView.context, "uthmanic.ttf"]
            two -> tvChapterContentArabic.typeface = TypeFace()[itemView.context, "fonts/quran_font.ttf"]
            three -> tvChapterContentArabic.typeface = TypeFace()[itemView.context, "fonts/droid_naskh.ttf"]
        }
    }

    private fun fontOther() {
        val one = preferences.getBoolean("$keyOtherFont${0}", true)
        val two = preferences.getBoolean("$keyOtherFont${1}", false)
        val three = preferences.getBoolean("$keyOtherFont${2}", false)

        when(true) {
            one -> {
                tvChapterContentTranscription.typeface = TypeFace()[itemView.context, "fonts/gilroy_light.ttf"]
                tvChapterContentTranslation.typeface = TypeFace()[itemView.context, "fonts/gilroy_extra.ttf"]
                tvChapterContentSource.typeface = TypeFace()[itemView.context, "fonts/gilroy_light.ttf"]
            }
            two -> {
                tvChapterContentTranscription.typeface = TypeFace()[itemView.context, "fonts/times_light.ttf"]
                tvChapterContentTranslation.typeface = TypeFace()[itemView.context, "fonts/times_medium.ttf"]
                tvChapterContentSource.typeface = TypeFace()[itemView.context, "fonts/times_light.ttf"]
            }
            three -> {
                tvChapterContentTranscription.typeface = TypeFace()[itemView.context, "fonts/cambria_light.ttf"]
                tvChapterContentTranslation.typeface = TypeFace()[itemView.context, "fonts/cambria_medium.ttf"]
                tvChapterContentSource.typeface = TypeFace()[itemView.context, "fonts/cambria_light.ttf"]
            }
        }
    }

    private fun textArabicSize() {
        tvChapterContentArabic.textSize = textSizes[preferences.getInt(keyArabicTextSize, 1)]
    }

    private fun textOtherSize() {
        tvChapterContentTranscription.textSize = textSizes[preferences.getInt(keyOtherTextSize, 1)]
        tvChapterContentTranslation.textSize = textSizes[preferences.getInt(keyOtherTextSize, 1)]
    }

    private fun textArabicColor() {
        tvChapterContentArabic.setTextColor(itemView.resources.getColor(
            textColorBackgrounds[preferences.getInt(keyArabicTextColor, 2)]))
    }

    private fun textTranscriptionColor() {
        tvChapterContentTranscription.setTextColor(
            itemView.resources.getColor(textColorBackgrounds[preferences.getInt(keyTranscriptionTextColor, 2)]))
        tvChapterContentSource.setTextColor(
            itemView.resources.getColor(textColorBackgrounds[preferences.getInt(keyTranscriptionTextColor, 2)]))
    }

    private fun textTranslationColor() {
        tvChapterContentTranslation.setTextColor(itemView.resources.getColor(
                textColorBackgrounds[preferences.getInt(keyTranslationTextColor, 2)]))
    }

    private fun showTranscription() {
        if (preferences.getBoolean(keyTranscriptionState, true)) {
            tvChapterContentTranscription.visibility = View.VISIBLE
        } else {
            tvChapterContentTranscription.visibility = View.GONE
        }
    }

    private fun showTranslation() {
        if (preferences.getBoolean(keyTranslationState, true)) {
            tvChapterContentTranslation.visibility = View.VISIBLE
        } else {
            tvChapterContentTranslation.visibility = View.GONE
        }
    }
}