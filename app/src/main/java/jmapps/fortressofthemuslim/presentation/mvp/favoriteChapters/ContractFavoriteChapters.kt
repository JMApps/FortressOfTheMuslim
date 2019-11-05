package jmapps.fortressofthemuslim.presentation.mvp.favoriteChapters

interface ContractFavoriteChapters {

    interface ViewFavoriteChapters {

        fun showFavoriteStateToast(state: Boolean)

        fun showDBExceptionToast(error: String)

        fun saveCurrentFavoriteItem(keyFavoriteChapter: String, stateFavoriteChapter: Boolean)
    }

    interface PresenterFavoriteChapters {

        fun addRemoveFavorite(state: Boolean, chapterId: Int)
    }
}