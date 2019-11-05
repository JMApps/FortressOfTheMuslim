package jmapps.fortressofthemuslim.data.database

import android.annotation.SuppressLint
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import jmapps.fortressofthemuslim.presentation.ui.favoriteChapters.ModelFavoriteChapters
import jmapps.fortressofthemuslim.presentation.ui.supplications.ModelSupplications

class DatabaseContents(private val context: Context?) {

    private lateinit var database: SQLiteDatabase

    val getSupplicationList: MutableList<ModelSupplications>
        @SuppressLint("Recycle")
        get() {

            database = DatabaseOpenHelper(context).readableDatabase

            val cursor: Cursor = database.query(
                "Table_of_dua",
                null,
                null,
                null,
                null,
                null,
                null
            )

            val supplicationList = ArrayList<ModelSupplications>()

            if (cursor.moveToFirst()) {
                while (!cursor.isAfterLast) {
                    val supplications = ModelSupplications(
                        cursor.getInt(cursor.getColumnIndex("_id")),
                        cursor.getString(cursor.getColumnIndex("content_arabic")),
                        cursor.getString(cursor.getColumnIndex("content_transcription")),
                        cursor.getString(cursor.getColumnIndex("content_translation")),
                        cursor.getString(cursor.getColumnIndex("content_source"))
                    )
                    supplicationList.add(supplications)
                    cursor.moveToNext()
                    if (cursor.isClosed) {
                        cursor.close()
                    }
                }
            }
            return supplicationList
        }

    val getFavoriteChapterList: MutableList<ModelFavoriteChapters>
        @SuppressLint("Recycle")
        get() {

            database = DatabaseOpenHelper(context).readableDatabase

            val cursor: Cursor = database.query(
                "Table_of_chapters",
                null,
                "chapter_favorite = 1",
                null,
                null,
                null,
                null
            )

            val favoriteChapterList = ArrayList<ModelFavoriteChapters>()

            if (cursor.moveToFirst()) {
                while (!cursor.isAfterLast) {
                    val favorites = ModelFavoriteChapters(
                        cursor.getInt(cursor.getColumnIndex("_id")),
                        cursor.getString(cursor.getColumnIndex("chapter_name"))
                    )
                    favoriteChapterList.add(favorites)
                    cursor.moveToNext()
                    if (cursor.isClosed) {
                        cursor.close()
                    }
                }
            }
            return favoriteChapterList
        }
}