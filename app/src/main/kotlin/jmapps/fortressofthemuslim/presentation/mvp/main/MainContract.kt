package jmapps.fortressofthemuslim.presentation.mvp.main

interface MainContract {

    interface MainView {

    }

    interface MainPresenter {

        fun setToastMessage(message: String)


        fun setAlertDialog()
    }
}