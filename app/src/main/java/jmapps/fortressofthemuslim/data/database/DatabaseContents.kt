package jmapps.fortressofthemuslim.data.database

import android.annotation.SuppressLint
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import jmapps.fortressofthemuslim.presentation.ui.favoriteSupplications.ModelFavoriteSupplications
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

    val getFavoriteSupplicationList: MutableList<ModelFavoriteSupplications>
        @SuppressLint("Recycle")
        get() {

            database = DatabaseOpenHelper(context).readableDatabase

            val cursor: Cursor = database.query(
                "Table_of_dua",
                null,
                "item_faOvorite = 1",
                null,
                null,
                null,
                null
            )

            val favoriteSupplicationList = ArrayList<ModelFavoriteSupplications>()

            if (cursor.moveToFirst()) {
                while (!cursor.isAfterLast) {
                    val supplications = ModelFavoriteSupplications(
                        cursor.getInt(cursor.getColumnIndex("_id")),
                        cursor.getString(cursor.getColumnIndex("content_arabic")),
                        cursor.getString(cursor.getColumnIndex("content_transcription")),
                        cursor.getString(cursor.getColumnIndex("content_translation")),
                        cursor.getString(cursor.getColumnIndex("content_source"))
                    )
                    favoriteSupplicationList.add(supplications)
                    cursor.moveToNext()
                    if (cursor.isClosed) {
                        cursor.close()
                    }
                }
            }
            return favoriteSupplicationList
        }
}