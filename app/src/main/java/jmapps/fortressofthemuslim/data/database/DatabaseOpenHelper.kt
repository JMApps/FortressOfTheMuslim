package jmapps.fortressofthemuslim.data.database

import android.content.Context
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper
import jmapps.fortressofthemuslim.R

private const val databaseVersion: Int = 1

class DatabaseOpenHelper(context: Context?) : SQLiteAssetHelper(
    context, context?.getString(R.string.database_name), null, databaseVersion) {

    init {
        setForcedUpgrade(databaseVersion)
    }
}