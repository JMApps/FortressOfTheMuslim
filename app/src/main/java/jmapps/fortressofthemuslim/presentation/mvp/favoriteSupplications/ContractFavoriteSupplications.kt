package jmapps.fortressofthemuslim.presentation.mvp.favoriteSupplications

interface ContractFavoriteSupplications {

    interface ViewFavoriteSupplications {

        fun showFavoriteSupplicationStateToast(state: Boolean)

        fun showDBExceptionSupplicationToast(error: String)

        fun saveCurrentFavoriteSupplicationItem(keyFavoriteSupplication: String, stateFavoriteSupplication: Boolean)
    }

    interface PresenterFavoriteSupplications {

        fun addRemoveFavoriteSupplication(state: Boolean, supplicationId: Int)
    }
}