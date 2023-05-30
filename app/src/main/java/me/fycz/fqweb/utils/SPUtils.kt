package me.fycz.fqweb.utils

import android.content.Context
import android.content.SharedPreferences

/**
 * @author fengyue
 * @date 2022/9/19 18:02
 */
object SPUtils {
    private lateinit var pref: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    fun init(context: Context) {
        pref = context.getSharedPreferences("FQWebConfig", Context.MODE_PRIVATE)
        editor = pref.edit()
    }

    fun putString(key: String, value: String?) {
        editor.putString(key, value)
        editor.apply()
    }

    fun putInt(key: String, value: Int) {
        editor.putInt(key, value)
        editor.apply()
    }

    fun putBoolean(key: String, value: Boolean) {
        editor.putBoolean(key, value)
        editor.apply()
    }

    fun putFloat(key: String, value: Float) {
        editor.putFloat(key, value)
        editor.apply()
    }

    fun putLong(key: String, value: Long) {
        editor.putLong(key, value)
        editor.apply()
    }


    fun getString(key: String, def: String = ""): String? {
        return pref.getString(key, def)
    }


    fun getInt(key: String, def: Int = 0): Int {
        return pref.getInt(key, def)
    }


    fun getBoolean(key: String, def: Boolean = false): Boolean {
        return pref.getBoolean(key, def)
    }


    fun getFloat(key: String, def: Float = 0F): Float {
        return pref.getFloat(key, def)
    }


    fun getLong(key: String, def: Long = 0): Long {
        return pref.getLong(key, def)
    }
}