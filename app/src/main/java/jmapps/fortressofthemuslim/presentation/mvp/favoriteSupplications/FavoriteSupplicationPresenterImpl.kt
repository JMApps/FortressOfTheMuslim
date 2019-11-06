package jmapps.fortressofthemuslim.presentation.mvp.favoriteSupplications

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase

class FavoriteSupplicationPresenterImpl(
    private val viewFavoriteSupplications: ContractFavoriteSupplications.ViewFavoriteSupplications?,
    private val database: SQLiteDatabase?) : ContractFavoriteSupplications.PresenterFavoriteSupplications {

    override fun addRemoveFavorite(state: Boolean, supplicationId: Int) {
        try {
            val favoriteChapter = ContentValues()
            favoriteChapter.put("item_favorite", state)

            database?.update(
                "Table_of_dua",
                favoriteChapter,
                "_id = ?",
                arrayOf(supplicationId.toString())
            )

            viewFavoriteSupplications?.showFavoriteStateToast(state)
            viewFavoriteSupplications?.saveCurrentFavoriteItem(
                "key_item_bookmark_§$supplicationId", state)

        } catch (e: Exception) {
            viewFavoriteSupplications?.showDBExceptionToast(e.toString())
        }
    }

}