package me.fycz.fqweb.utils

import java.io.File
import java.text.SimpleDateFormat

/**
 * @author fengyue
 * @date 2023/5/28 17:02
 * @description
 */
object LogUtils {
    private val logFile: File by lazy {
        File(GlobalApp.application?.cacheDir?.absolutePath + "/MiCat.log")
    }

    private val dateFormat = SimpleDateFormat("[yyyy-MM-dd HH:mm:ss]")

    fun log(msg: String) {
        if (GlobalApp.hasInit())
            logFile.appendText("${dateFormat.format(System.currentTimeMillis())} $msg\n")
    }
}