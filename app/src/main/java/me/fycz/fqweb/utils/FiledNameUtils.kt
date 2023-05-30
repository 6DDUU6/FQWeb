package me.fycz.fqweb.utils

import java.util.Locale
import java.util.regex.Matcher
import java.util.regex.Pattern


/**
 * @author fengyue
 * @date 2023/5/30 10:46
 * @description
 */
object FiledNameUtils {

    // 自定义正则表达式
    private val HUMP_PATTERN: Pattern = Pattern.compile("[A-Z0-9]")

    fun humpToLine(str: String): String {
        val matcher: Matcher = HUMP_PATTERN.matcher(str)
        val sb = StringBuffer()
        while (matcher.find()) {
            matcher.appendReplacement(sb, "_" + matcher.group(0).lowercase(Locale.getDefault()))
        }
        matcher.appendTail(sb)
        return sb.toString()
    }

    private const val UNDERLINE = '_'

    fun underlineToCamel(param: String): String {
        if (param.isBlank()) {
            return ""
        }
        val len = param.length
        val sb = StringBuilder(len)
        var i = 0
        while (i < len) {
            val c = param[i].lowercaseChar()
            if (c == UNDERLINE) {
                if (++i < len) {
                    sb.append(param[i].uppercaseChar())
                }
            } else {
                sb.append(c)
            }
            i++
        }
        return sb.toString()
    }
}