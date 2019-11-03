package jmapps.fortressofthemuslim.data.database

import android.annotation.SuppressLint
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import jmapps.fortressofthemuslim.presentation.ui.chapters.ModelChapters

class DatabaseLists(private val context: Context?) {

    private lateinit var database: SQLiteDatabase

    val getChapterList: MutableList<ModelChapters>
        @SuppressLint("Recycle")
        get() {

            database = DatabaseOpenHelper(context).readableDatabase

            val cursor: Cursor = database.query(
                "Table_of_chapters",
                null,
                null,
                null,
                null,
                null,
                null
            )

            val chapterList = ArrayList<ModelChapters>()

            if (cursor.moveToFirst()) {
                while (!cursor.isAfterLast) {
                    val chapters = ModelChapters(
                        cursor.getInt(cursor.getColumnIndex("_id")),
                        cursor.getString(cursor.getColumnIndex("chapter_name"))
                    )
                    chapterList.add(chapters)
                    cursor.moveToNext()
                    if (cursor.isClosed) {
                        cursor.close()
                    }
                }
            }
            return chapterList
        }
//
//    val getFavoriteList: MutableList<ModelFavorites>
//        @SuppressLint("Recycle")
//        get() {
//
//            database = DatabaseAsset(context).readableDatabase
//
//            val cursor: Cursor = database.query(
//                "Table_question",
//                null,
//                "Favorite = 1",
//                null,
//                null,
//                null,
//                null
//            )
//
//            val favoriteList = ArrayList<ModelFavorites>()
//
//            if (cursor.moveToFirst()) {
//                while (!cursor.isAfterLast) {
//                    val favorites = ModelFavorites(
//                        cursor.getString(cursor.getColumnIndex("_id")),
//                        cursor.getString(cursor.getColumnIndex("Question_number")),
//                        cursor.getString(cursor.getColumnIndex("Question_content"))
//                    )
//                    favoriteList.add(favorites)
//                    cursor.moveToNext()
//                    if (cursor.isClosed) {
//                        cursor.close()
//                    }
//                }
//            }
//            return favoriteList
//        }
}