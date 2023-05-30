package me.fycz.fqweb.web.controller

import me.fycz.fqweb.utils.getObjectField
import me.fycz.fqweb.web.ReturnData
import me.fycz.fqweb.web.service.DragonService


/**
 * @author fengyue
 * @date 2023/5/29 18:04
 * @description
 */
object DragonController {

    fun search(parameters: Map<String, List<String>>): ReturnData {
        val keyword = parameters["query"]?.firstOrNull()
        val returnData = ReturnData()
        if (keyword.isNullOrEmpty()) {
            return returnData.setErrorMsg("参数query不能为空")
        }
        returnData.setData(DragonService.search(keyword))
        return returnData
    }

    fun info(parameters: Map<String, MutableList<String>>): ReturnData {
        val bookId = parameters["book_id"]?.firstOrNull()
        val returnData = ReturnData()
        if (bookId.isNullOrEmpty()) {
            return returnData.setErrorMsg("参数book_id不能为空")
        }
        returnData.setData(DragonService.getInfo(bookId))
        return returnData
    }

    fun catalog(parameters: Map<String, MutableList<String>>): ReturnData {
        val bookId = parameters["book_id"]?.firstOrNull()
        val returnData = ReturnData()
        if (bookId.isNullOrEmpty()) {
            return returnData.setErrorMsg("参数book_id不能为空")
        }
        returnData.setData(DragonService.getCatalog(bookId))
        return returnData
    }

    fun content(parameters: Map<String, MutableList<String>>): ReturnData {
        val itemId = parameters["item_id"]?.firstOrNull()
        val returnData = ReturnData()
        if (itemId.isNullOrEmpty()) {
            return returnData.setErrorMsg("参数item_id不能为空")
        }
        val content = DragonService.getContent(itemId)
        DragonService.decodeContent(content.getObjectField("data") as Any)
        returnData.setData(content)
        return returnData
    }

    fun bookMall(parameters: Map<String, MutableList<String>>): ReturnData {
        val returnData = ReturnData()
        returnData.setData(DragonService.bookMall(parameters))
        return returnData
    }

    fun newCategory(parameters: Map<String, MutableList<String>>): ReturnData {
        val returnData = ReturnData()
        returnData.setData(DragonService.newCategory(parameters))
        return returnData
    }

}