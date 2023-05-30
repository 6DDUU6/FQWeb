package me.fycz.fqweb.utils

import de.robv.android.xposed.XSharedPreferences
/**
 * @author fengyue
 * @date 2022/9/19 17:23
 */
object XSPUtils {
    private var prefs = XSharedPreferences("me.fycz.FQWeb", "config")

    fun getBoolean(key: String, defValue: Boolean = false): Boolean {
        if (prefs.hasFileChanged()) {
            prefs.reload()
        }
        return prefs.getBoolean(key, defValue)
    }

    fun getInt(key: String, defValue: Int = 0): Int {
        if (prefs.hasFileChanged()) {
            prefs.reload()
        }
        return prefs.getInt(key, defValue)
    }

    fun getFloat(key: String, defValue: Float = 0F): Float {
        if (prefs.hasFileChanged()) {
            prefs.reload()
        }
        return prefs.getFloat(key, defValue)
    }

    fun getString(key: String, defValue: String = ""): String? {
        if (prefs.hasFileChanged()) {
            prefs.reload()
        }
        return prefs.getString(key, defValue)
    }

    fun getAll(): Map<String, *> {
        if (prefs.hasFileChanged()) {
            prefs.reload()
        }
        return prefs.all
    }
}

inline fun hasEnable(
    key: String,
    default: Boolean = false,
    noinline extraCondition: (() -> Boolean)? = null,
    crossinline block: () -> Unit
) {
    val conditionResult = if (extraCondition != null) extraCondition() else true
    if (XSPUtils.getBoolean(key, default) && conditionResult) {
        block()
    }
}