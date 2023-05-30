package me.fycz.fqweb.utils

/**
 * @author fengyue
 * @date 2023/5/30 9:51
 * @description
 */
object JsonUtils {

    private val jsonUtils by lazy {
        "com.dragon.read.base.util.JSONUtils".findClass(GlobalApp.getClassloader())
    }

    private val gson by lazy {
        jsonUtils.getStaticObjectField("gson")!!
    }

    fun toJson(obj: Any): String{
        return jsonUtils.callStaticMethod("toJson", obj) as String
    }

    fun toJson(src: Any, writer: Any){
        gson.callMethod("toJson", src, writer)
    }
}