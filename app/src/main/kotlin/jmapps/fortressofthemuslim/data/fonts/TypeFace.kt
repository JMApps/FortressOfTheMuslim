package jmapps.fortressofthemuslim.data.fonts

import android.content.Context
import android.graphics.Typeface
import java.util.*

class TypeFace {

    private val cache = Hashtable<String, Typeface>()

    operator fun get(context: Context?, name: String): Typeface? {

        synchronized(cache) {
            if (!cache.containsKey(name)) {
                try {
                    val t = Typeface.createFromAsset(context?.assets, name)
                    cache[name] = t
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            return cache[name]
        }
    }
}