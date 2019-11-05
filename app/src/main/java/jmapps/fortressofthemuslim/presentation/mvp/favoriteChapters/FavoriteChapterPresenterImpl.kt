package jmapps.fortressofthemuslim.presentation.mvp.favoriteChapters

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import java.lang.Exception

class FavoriteChapterPresenterImpl(
    private val viewFavoriteChapters: ContractFavoriteChapters.ViewFavoriteChapters?,
    private val database: SQLiteDatabase?) :
    ContractFavoriteChapters.PresenterFavoriteChapters {

    override fun addRemoveFavorite(state: Boolean, chapterId: Int) {
        try {
            val favoriteChapter = ContentValues()
            favoriteChapter.put("chapter_favorite", state)

            database?.update("Table_of_chapters",
                favoriteChapter,
                "_id = ?",
                arrayOf(chapterId.toString()))

            viewFavoriteChapters?.showFavoriteStateToast(state)
            viewFavoriteChapters?.saveCurrentFavoriteItem("key_chapter_bookmark_$chapterId", state)

        } catch (e: Exception) {
            viewFavoriteChapters?.showDBExceptionToast(e.toString())
        }
    }
}