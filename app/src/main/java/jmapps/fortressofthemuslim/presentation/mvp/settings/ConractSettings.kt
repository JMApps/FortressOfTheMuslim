package jmapps.fortressofthemuslim.presentation.mvp.settings

interface ContractSettings {

    interface SettingsView {

        fun textArabicFont(fontName: String)

        fun textTranscriptionFont(fontName: String)

        fun textTranslationFont(fontName: String)

        fun textArabicSize(size: Float)

        fun textOtherSize(size: Float)

        fun transcriptionState(state: Boolean)

        fun translationState(state: Boolean)
    }

    interface SettingsPresenter {

        fun setArabicFont(mode: Int)

        fun setOtherFont(mode: Int)

        fun setArabicTextSize(mode: Int)

        fun setOtherTextSize(mode: Int)

        fun setTranscriptionState(state: Boolean)

        fun setTranslationState(state: Boolean)
    }
}