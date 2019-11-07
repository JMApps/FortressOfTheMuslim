package jmapps.fortressofthemuslim.data.database

import android.annotation.SuppressLint
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import jmapps.fortressofthemuslim.data.files.ModelSupplicationLink

class DatabaseLinkLists(private val context: Context?) {

    private lateinit var sqLiteDatabase: SQLiteDatabase

    val getSupplicationLinksList: MutableList<ModelSupplicationLink>
        @SuppressLint("Recycle") get() {

            sqLiteDatabase = DatabaseOpenHelper(context).readableDatabase

            val cursor: Cursor = sqLiteDatabase.query(
                "Table_audio_links",
                null,
                null,
                null,
                null,
                null,
                null,
                null)

            val supplicationLinksList = ArrayList<ModelSupplicationLink>()

            if (cursor.moveToFirst()) {
                while (!cursor.isAfterLast) {
                    val supplicationLinks = ModelSupplicationLink(
                        cursor.getInt(cursor.getColumnIndex("_id")),
                        cursor.getString(cursor.getColumnIndex("audio_link")))
                    supplicationLinksList.add(supplicationLinks)
                    cursor.moveToNext()
                    if (cursor.isClosed) {
                        cursor.close()
                    }
                }
            }
            return supplicationLinksList
        }

    fun getSupplicationSelectiveList(sampleBy: Int): MutableList<ModelSupplicationLink> {
        sqLiteDatabase = DatabaseOpenHelper(context).readableDatabase

        val cursor: Cursor = sqLiteDatabase.query(
            "Table_audio_links",
            null,
            "sample_by = $sampleBy",
            null,
            null,
            null,
            null,
            null)

        val supplicationLinksList = ArrayList<ModelSupplicationLink>()

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast) {
                val supplicationLinks = ModelSupplicationLink(
                    cursor.getInt(cursor.getColumnIndex("_id")),
                    cursor.getString(cursor.getColumnIndex("audio_link")))
                supplicationLinksList.add(supplicationLinks)
                cursor.moveToNext()
                if (cursor.isClosed) {
                    cursor.close()
                }
            }
        }
        return supplicationLinksList
    }
}