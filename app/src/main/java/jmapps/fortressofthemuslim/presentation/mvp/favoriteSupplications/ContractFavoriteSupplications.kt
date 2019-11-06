package jmapps.fortressofthemuslim.presentation.mvp.favoriteSupplications

interface ContractFavoriteSupplications {

    interface ViewFavoriteSupplications {

        fun showFavoriteStateToast(state: Boolean)

        fun showDBExceptionToast(error: String)

        fun saveCurrentFavoriteItem(keyFavoriteSupplication: String, stateFavoriteSupplication: Boolean)
    }

    interface PresenterFavoriteSupplications {

        fun addRemoveFavorite(state: Boolean, supplicationId: Int)
    }
}