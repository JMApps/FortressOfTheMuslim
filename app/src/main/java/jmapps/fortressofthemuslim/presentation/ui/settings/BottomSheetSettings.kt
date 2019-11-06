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

class BottomSheetSettings : BottomSheetDialogFragment(), RadioGroup.OnCheckedChangeListener,
    SeekBar.OnSeekBarChangeListener, CompoundButton.OnCheckedChangeListener {

    private lateinit var rootSettings: View

    private lateinit var preferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    private lateinit var textSizeValues: ArrayList<Int>

    @SuppressLint("CommitPrefEdits")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        rootSettings = inflater.inflate(R.layout.bottom_sheet_settings, container, false)

        preferences = PreferenceManager.getDefaultSharedPreferences(context)
        editor = preferences.edit()

        textSizeValues = ArrayList()
        textSizeValues.add(12)
        textSizeValues.add(14)
        textSizeValues.add(16)
        textSizeValues.add(18)
        textSizeValues.add(20)
        textSizeValues.add(22)
        textSizeValues.add(24)
        textSizeValues.add(26)
        textSizeValues.add(28)
        textSizeValues.add(30)

        rootSettings.rbFontArabicOne.isChecked = preferences.getBoolean("key_arabic_font_one", true)
        rootSettings.rbFontArabicTwo.isChecked = preferences.getBoolean("key_arabic_font_two", false)
        rootSettings.rbFontArabicThree.isChecked = preferences.getBoolean("key_arabic_font_three", false)

        rootSettings.rbFontOtherOne.isChecked = preferences.getBoolean("key_other_font_one", true)
        rootSettings.rbFontOtherTwo.isChecked = preferences.getBoolean("key_other_font_two", false)
        rootSettings.rbFontOtherThree.isChecked = preferences.getBoolean("key_other_font_three", false)

        rootSettings.sbArabicTextSize.progress = preferences.getInt("key_arabic_text_size", 3)
        rootSettings.sbOtherTextSize.progress = preferences.getInt("key_other_text_size", 3)

        rootSettings.swShowTextTranscription.isChecked = preferences.getBoolean("key_transcription_state", true)
        rootSettings.swShowTextTranslation.isChecked = preferences.getBoolean("key_translation_state", true)

        rootSettings.rgArabicFonts.setOnCheckedChangeListener(this)
        rootSettings.rgOtherFonts.setOnCheckedChangeListener(this)

        rootSettings.sbArabicTextSize.setOnSeekBarChangeListener(this)
        rootSettings.sbOtherTextSize.setOnSeekBarChangeListener(this)

        rootSettings.swShowTextTranscription.setOnCheckedChangeListener(this)
        rootSettings.swShowTextTranslation.setOnCheckedChangeListener(this)

        return rootSettings
    }

    override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
        when(group?.id) {

            R.id.rgArabicFonts -> {
                editor.putBoolean("key_arabic_font_one", rootSettings.rbFontArabicOne.isChecked).apply()
                editor.putBoolean("key_arabic_font_two", rootSettings.rbFontArabicTwo.isChecked).apply()
                editor.putBoolean("key_arabic_font_three", rootSettings.rbFontArabicThree.isChecked).apply()
            }

            R.id.rgOtherFonts -> {
                editor.putBoolean("key_other_font_one", rootSettings.rbFontOtherOne.isChecked).apply()
                editor.putBoolean("key_other_font_two", rootSettings.rbFontOtherTwo.isChecked).apply()
                editor.putBoolean("key_other_font_three", rootSettings.rbFontOtherThree.isChecked).apply()
            }
        }
    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        when(seekBar?.id) {

            R.id.sbArabicTextSize -> {
                editor.putInt("key_arabic_text_size", progress).apply()
                setToast("${textSizeValues[progress]}")
            }

            R.id.sbOtherTextSize -> {
                editor.putInt("key_other_text_size", progress).apply()
                setToast("${textSizeValues[progress]}")
            }
        }
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {}

    override fun onStopTrackingTouch(seekBar: SeekBar?) {}

    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        when(buttonView?.id) {

            R.id.swShowTextTranscription -> {
                if (isChecked) {
                     setToast(getString(R.string.action_show_state_transcription_off))
                } else {
                    setToast(getString(R.string.action_show_state_transcription_on))
                }
                editor.putBoolean("key_transcription_state", isChecked).apply()
            }

            R.id.swShowTextTranslation -> {
                if (isChecked) {
                    setToast(getString(R.string.action_show_state_translation_off))
                } else {
                    setToast(getString(R.string.action_show_state_translation_on))
                }
                editor.putBoolean("key_translation_state", isChecked).apply()
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
}