package me.fycz.fqweb.web.service

import me.fycz.fqweb.constant.Config
import me.fycz.fqweb.utils.FiledNameUtils
import me.fycz.fqweb.utils.GlobalApp
import me.fycz.fqweb.utils.callMethod
import me.fycz.fqweb.utils.callStaticMethod
import me.fycz.fqweb.utils.findClass
import me.fycz.fqweb.utils.getObjectField
import me.fycz.fqweb.utils.new
import me.fycz.fqweb.utils.setBooleanField
import me.fycz.fqweb.utils.setFloatField
import me.fycz.fqweb.utils.setIntField
import me.fycz.fqweb.utils.setLongField
import me.fycz.fqweb.utils.setObjectField
import me.fycz.fqweb.utils.setShortField

/**
 * @author fengyue
 * @date 2023/5/30 10:54
 * @description
 */
object DragonService {

    private val dragonClassLoader: ClassLoader by lazy {
        GlobalApp.getClassloader()
    }

    fun search(keyword: String, page: Int = 1): Any {
        val GetSearchPageRequest =
            "${Config.rpcModelPackage}.GetSearchPageRequest".findClass(dragonClassLoader)
        val getSearchPageRequest = GetSearchPageRequest.newInstance()
        getSearchPageRequest.setIntField("bookshelfSearchPlan", 4)
        getSearchPageRequest.setIntField("bookstoreTab", 2)
        getSearchPageRequest.setObjectField("clickedContent", "page_search_button")
        getSearchPageRequest.setObjectField("query", keyword)
        getSearchPageRequest.setObjectField(
            "searchSource",
            "${Config.rpcModelPackage}.SearchSource"
                .findClass(dragonClassLoader)
                .callStaticMethod("findByValue", arrayOf(Int::class.java), 1)
        )
        getSearchPageRequest.setObjectField("searchSourceId", "clks###")
        getSearchPageRequest.setObjectField("tabName", "store")
        getSearchPageRequest.setObjectField(
            "tabType", "${Config.rpcModelPackage}.SearchTabType"
                .findClass(dragonClassLoader)
                .callStaticMethod("findByValue", arrayOf(Int::class.java), 1)
        )
        getSearchPageRequest.setShortField("userIsLogin", 1)
        setField(getSearchPageRequest, "offset", (page - 1) * 10)
        setField(getSearchPageRequest, "passback", (page - 1) * 10)
        return callFunction(
            clzName = "${Config.rpcApiPackage}.a",
            obj = getSearchPageRequest
        )
    }

    fun getInfo(bookId: String): Any {
        val BookDetailRequest =
            "${Config.rpcModelPackage}.BookDetailRequest".findClass(dragonClassLoader)
        val bookDetailRequest = BookDetailRequest.newInstance()
        bookDetailRequest.setLongField("bookId", bookId.toLong())
        return callFunction(
            clzName = "${Config.rpcApiPackage}.a",
            obj = bookDetailRequest
        )
    }

    fun getCatalog(bookId: String): Any {
        val GetDirectoryForItemIdRequest =
            "${Config.rpcModelPackage}.GetDirectoryForItemIdRequest".findClass(dragonClassLoader)
        val getDirectoryForItemIdRequest = GetDirectoryForItemIdRequest.newInstance()
        getDirectoryForItemIdRequest.setObjectField("bookId", bookId.toLong())
        return callFunction(
            clzName = "${Config.rpcApiPackage}.a",
            obj = getDirectoryForItemIdRequest
        )
    }

    fun getContent(itemId: String): Any {
        val FullRequest =
            "${Config.rpcModelPackage}.FullRequest".findClass(dragonClassLoader)
        val fullRequest = FullRequest.newInstance()
        fullRequest.setObjectField("itemId", itemId)
        return callFunction(
            clzName = "${Config.rpcApiPackage}.e",
            obj = fullRequest
        )
    }

    fun decodeContent(itemContent: Any): Any {
        return "com.dragon.read.reader.bookend.a.a".findClass(dragonClassLoader)
            .new(null).callMethod("a", itemContent)!!.callMethod("blockingFirst")!!
    }

    fun bookMall(parameters: Map<String, MutableList<String>>): Any {
        val GetBookMallCellChangeRequest =
            "${Config.rpcModelPackage}.GetBookMallCellChangeRequest".findClass(dragonClassLoader)
        val getBookMallCellChangeRequest = GetBookMallCellChangeRequest.newInstance()
        parameters.forEach { (key, value) ->
            setField(getBookMallCellChangeRequest, key, value.first())
        }
        return callFunction(
            clzName = "${Config.rpcApiPackage}.a",
            obj = getBookMallCellChangeRequest
        )
    }

    fun newCategory(parameters: Map<String, MutableList<String>>): Any {
        val GetNewCategoryLandingPageRequest =
            "${Config.rpcModelPackage}.GetNewCategoryLandingPageRequest".findClass(dragonClassLoader)
        val getNewCategoryLandingPageRequest = GetNewCategoryLandingPageRequest.newInstance()
        parameters.forEach { (key, value) ->
            setField(getNewCategoryLandingPageRequest, key, value.first())
        }
        return callFunction(
            clzName = "${Config.rpcApiPackage}.a",
            obj = getNewCategoryLandingPageRequest
        )
    }

    private fun callFunction(clzName: String, funcName: String = "a", obj: Any): Any {
        return try {
            clzName.findClass(dragonClassLoader)
                .callStaticMethod(
                    funcName,
                    obj
                )!!.callMethod("blockingFirst")!!
        } catch (e: Throwable) {
            e.stackTraceToString()
        }
    }

    private fun setField(obj: Any, name: String, value: Any) {
        try {
            val fieldName = FiledNameUtils.underlineToCamel(name)
            val fieldValueStr = value.toString()
            when (val field = obj.getObjectField(fieldName)!!) {
                is Short -> obj.setShortField(fieldName, fieldValueStr.toShort())
                is Int -> obj.setIntField(fieldName, fieldValueStr.toInt())
                is Long -> obj.setLongField(fieldName, fieldValueStr.toLong())
                is Float -> obj.setFloatField(fieldName, fieldValueStr.toFloat())
                is Boolean -> obj.setBooleanField(fieldName, fieldValueStr.toBoolean())
                else -> {
                    val fieldClz = field.javaClass
                    if (fieldClz.isEnum) {
                        obj.setObjectField(fieldName, fieldClz.callStaticMethod("findByValue", fieldValueStr.toInt()))
                    } else {
                        //String
                        obj.setObjectField(fieldName, value)
                    }
                }
            }
        } catch (e: Throwable) {
            //log(e)
        }
    }
}