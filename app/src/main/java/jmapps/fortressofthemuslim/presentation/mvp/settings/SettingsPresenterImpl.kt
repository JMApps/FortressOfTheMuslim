package jmapps.fortressofthemuslim.presentation.mvp.settings

class SettingsPresenterImpl(private val settingsView: ContractSettings.SettingsView?):
        ContractSettings.SettingsPresenter {

    override fun setArabicFont(mode: Int) {
        when (mode) {
            1 -> settingsView?.textArabicFont("/fonts/droid_naksh.ttf")

            2 -> settingsView?.textArabicFont("/fonts/droid_naksh.ttf")

            3 -> settingsView?.textArabicFont("/fonts/droid_naksh.ttf")
        }
    }

    override fun setOtherFont(mode: Int) {
        when (mode) {
            1 -> {
                settingsView?.textTranscriptionFont("/fonts/gilroy_light.ttf")
                settingsView?.textTranslationFont("/fonts/gilroy_medium.ttf")
            }

            2 -> {
                settingsView?.textTranscriptionFont("/fonts/gilroy_light.ttf")
                settingsView?.textTranslationFont("/fonts/gilroy_medium.ttf")
            }

            3 -> {
                settingsView?.textTranscriptionFont("/fonts/gilroy_light.ttf")
                settingsView?.textTranslationFont("/fonts/gilroy_medium.ttf")
            }
        }
    }

    override fun setArabicTextSize(mode: Int) {
        when (mode) {
            1 -> settingsView?.textArabicSize(12f)

            2 -> settingsView?.textArabicSize(14f)

            3 -> settingsView?.textArabicSize(16f)

            4 -> settingsView?.textArabicSize(18f)

            5 -> settingsView?.textArabicSize(20f)

            6 -> settingsView?.textArabicSize(22f)

            7 -> settingsView?.textArabicSize(24f)

            8 -> settingsView?.textArabicSize(26f)

            9 -> settingsView?.textArabicSize(28f)

            10 -> settingsView?.textArabicSize(30f)
        }
    }

    override fun setOtherTextSize(mode: Int) {
        when (mode) {
            1 -> settingsView?.textOtherSize(12f)

            2 -> settingsView?.textOtherSize(14f)

            3 -> settingsView?.textOtherSize(16f)

            4 -> settingsView?.textOtherSize(18f)

            5 -> settingsView?.textOtherSize(20f)

            6 -> settingsView?.textOtherSize(22f)

            7 -> settingsView?.textOtherSize(24f)

            8 -> settingsView?.textOtherSize(26f)

            9 -> settingsView?.textOtherSize(28f)

            10 -> settingsView?.textOtherSize(30f)
        }
    }

    override fun setTranscriptionState(state: Boolean) {
        settingsView?.transcriptionState(state)
    }

    override fun setTranslationState(state: Boolean) {
        settingsView?.translationState(state)
    }
}