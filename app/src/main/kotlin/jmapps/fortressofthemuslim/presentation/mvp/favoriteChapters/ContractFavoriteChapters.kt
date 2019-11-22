package jmapps.fortressofthemuslim.presentation.mvp.favoriteChapters

interface ContractFavoriteChapters {

    interface ViewFavoriteChapters {

        fun showFavoriteChapterStateToast(state: Boolean)

        fun showDBExceptionChapterToast(error: String)

        fun saveCurrentFavoriteChapterItem(keyFavoriteChapter: String, stateFavoriteChapter: Boolean)
    }

    interface PresenterFavoriteChapters {

        fun addRemoveFavoriteChapter(state: Boolean, chapterId: Int)
    }
}