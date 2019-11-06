package jmapps.fortressofthemuslim.presentation.ui.settings

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.preference.PreferenceManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import jmapps.fortressofthemuslim.R
import kotlinx.android.synthetic.main.bottom_sheet_settings.view.*
import java.util.*
import android.graphics.drawable.GradientDrawable

class BottomSheetSettings : BottomSheetDialogFragment(), RadioGroup.OnCheckedChangeListener,
    SeekBar.OnSeekBarChangeListener, CompoundButton.OnCheckedChangeListener {

    private lateinit var rootSettings: View

    private lateinit var preferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    private var textSizeValues: ArrayList<Int> = ArrayList()
    private var textColorNames: ArrayList<String> = ArrayList()
    private var textColorValues: ArrayList<Int> = ArrayList()
    private var textColorBackgrounds: ArrayList<Int> = ArrayList()

    private val keyArabicFont = "key_arabic_font_"
    private val keyOtherFont = "key_other_font_"

    private val keyArabicTextSize = "key_arabic_text_size"
    private val keyOtherTextSize = "key_other_text_size"

    private val keyArabicTextColor = "key_arabic_text_color"
    private val keyTranscriptionTextColor = "key_transcription_text_color"
    private val keyTranslationTextColor = "key_translation_text_color"

    private val keyTranscriptionState = "key_transcription_state"
    private val keyTranslationState = "key_translation_state"

    init {
        textSizeValues.add(16)
        textSizeValues.add(18)
        textSizeValues.add(20)
        textSizeValues.add(22)
        textSizeValues.add(24)
        textSizeValues.add(26)
        textSizeValues.add(28)
        textSizeValues.add(30)

        textColorNames.add("Белый")
        textColorNames.add("Черный")
        textColorNames.add("Серый")
        textColorNames.add("Коричневый")
        textColorNames.add("Жёлтый")
        textColorNames.add("Зеленый")
        textColorNames.add("Синий")
        textColorNames.add("Красный")
        textColorNames.add("Фиолетовый")

        textColorValues.add(R.color.black)
        textColorValues.add(R.color.white)
        textColorValues.add(R.color.white)
        textColorValues.add(R.color.white)
        textColorValues.add(R.color.white)
        textColorValues.add(R.color.white)
        textColorValues.add(R.color.white)
        textColorValues.add(R.color.white)
        textColorValues.add(R.color.white)

        textColorBackgrounds.add(R.color.white)
        textColorBackgrounds.add(R.color.black)
        textColorBackgrounds.add(R.color.gray)
        textColorBackgrounds.add(R.color.brown)
        textColorBackgrounds.add(R.color.yellow)
        textColorBackgrounds.add(R.color.green)
        textColorBackgrounds.add(R.color.blue)
        textColorBackgrounds.add(R.color.red)
        textColorBackgrounds.add(R.color.purple)
    }

    @SuppressLint("CommitPrefEdits")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootSettings = inflater.inflate(R.layout.bottom_sheet_settings, container, false)

        preferences = PreferenceManager.getDefaultSharedPreferences(context)
        editor = preferences.edit()

        rootSettings.rbFontArabicOne.isChecked = preferences.getBoolean("$keyArabicFont${0}", true)
        rootSettings.rbFontArabicTwo.isChecked = preferences.getBoolean("$keyArabicFont${1}", false)
        rootSettings.rbFontArabicThree.isChecked = preferences.getBoolean("$keyArabicFont${2}", false)

        rootSettings.rbFontOtherOne.isChecked = preferences.getBoolean("$keyOtherFont${0}", true)
        rootSettings.rbFontOtherTwo.isChecked = preferences.getBoolean("$keyOtherFont${1}", false)
        rootSettings.rbFontOtherThree.isChecked = preferences.getBoolean("$keyOtherFont${2}", false)

        rootSettings.sbArabicTextSize.progress = preferences.getInt(keyArabicTextSize, 1)
        rootSettings.sbOtherTextSize.progress = preferences.getInt(keyOtherTextSize, 1)
        rootSettings.sbArabicTextColor.progress = preferences.getInt(keyArabicTextColor, 2)
        rootSettings.sbTranscriptionTextColor.progress = preferences.getInt(keyTranscriptionTextColor, 2)
        rootSettings.sbTranslationTextColor.progress = preferences.getInt(keyTranslationTextColor, 2)

        rootSettings.swShowTextTranscription.isChecked = preferences.getBoolean(keyTranscriptionState, true)
        rootSettings.swShowTextTranslation.isChecked = preferences.getBoolean(keyTranslationState, true)

        rootSettings.rgArabicFonts.setOnCheckedChangeListener(this)
        rootSettings.rgOtherFonts.setOnCheckedChangeListener(this)

        rootSettings.sbArabicTextSize.setOnSeekBarChangeListener(this)
        rootSettings.sbOtherTextSize.setOnSeekBarChangeListener(this)
        rootSettings.sbArabicTextColor.setOnSeekBarChangeListener(this)
        rootSettings.sbTranscriptionTextColor.setOnSeekBarChangeListener(this)
        rootSettings.sbTranslationTextColor.setOnSeekBarChangeListener(this)

        rootSettings.swShowTextTranscription.setOnCheckedChangeListener(this)
        rootSettings.swShowTextTranslation.setOnCheckedChangeListener(this)

        return rootSettings
    }

    override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
        when (group?.id) {

            R.id.rgArabicFonts -> {
                editor.putBoolean("$keyArabicFont${0}", rootSettings.rbFontArabicOne.isChecked).apply()
                editor.putBoolean("$keyArabicFont${1}", rootSettings.rbFontArabicTwo.isChecked).apply()
                editor.putBoolean("$keyArabicFont${2}", rootSettings.rbFontArabicThree.isChecked).apply()
            }

            R.id.rgOtherFonts -> {
                editor.putBoolean("$keyOtherFont${0}", rootSettings.rbFontOtherOne.isChecked).apply()
                editor.putBoolean("$keyOtherFont${1}", rootSettings.rbFontOtherTwo.isChecked).apply()
                editor.putBoolean("$keyOtherFont${2}", rootSettings.rbFontOtherThree.isChecked).apply()
            }
        }
    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        when (seekBar?.id) {

            R.id.sbArabicTextSize -> {
                editor.putInt(keyArabicTextSize, progress).apply()
                setToast("${textSizeValues[progress]}")
            }

            R.id.sbOtherTextSize -> {
                editor.putInt(keyOtherTextSize, progress).apply()
                setToast("${textSizeValues[progress]}")
            }

            R.id.sbArabicTextColor -> {
                editor.putInt(keyArabicTextColor, progress).apply()
                setToastWithColor(
                    textColorNames[progress], textColorValues[progress], textColorBackgrounds[progress])
            }

            R.id.sbTranscriptionTextColor -> {
                editor.putInt(keyTranscriptionTextColor, progress).apply()
                setToastWithColor(
                    textColorNames[progress], textColorValues[progress], textColorBackgrounds[progress])
            }

            R.id.sbTranslationTextColor -> {
                editor.putInt(keyTranslationTextColor, progress).apply()
                setToastWithColor(
                    textColorNames[progress], textColorValues[progress], textColorBackgrounds[progress])
            }
        }
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {}

    override fun onStopTrackingTouch(seekBar: SeekBar?) {}

    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        when (buttonView?.id) {

            R.id.swShowTextTranscription -> {
                editor.putBoolean(keyTranscriptionState, isChecked).apply()
                if (isChecked) {
                    setToast(getString(R.string.action_show_state_transcription_off))
                } else {
                    setToast(getString(R.string.action_show_state_transcription_on))
                }
            }

            R.id.swShowTextTranslation -> {
                editor.putBoolean(keyTranslationState, isChecked).apply()
                if (isChecked) {
                    setToast(getString(R.string.action_show_state_translation_off))
                } else {
                    setToast(getString(R.string.action_show_state_translation_on))
                }
            }
        }
    }

    private fun setToast(message: String) {
        val toast = Toast.makeText(context, message, Toast.LENGTH_LONG)
        val view: View = toast.view
        view.setBackgroundResource(R.drawable.circle_toast_background)
        val text = view.findViewById(android.R.id.message) as TextView
        text.setPadding(32, 16, 32, 16)
        text.setTextColor(resources.getColor(R.color.white))
        toast.show()
    }

    private fun setToastWithColor(message: String, textColor: Int, backgroundColor: Int) {
        val toast = Toast.makeText(context, message, Toast.LENGTH_LONG)
        val view: View = toast.view
        val text = view.findViewById(android.R.id.message) as TextView
        val gd = GradientDrawable()
        gd.cornerRadius = 25F
        gd.setColor(resources.getColor(backgroundColor))
        view.background = gd
        text.setPadding(120, 16, 120, 16)
        text.setTextColor(resources.getColor(textColor))
        toast.show()
    }
}