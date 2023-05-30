package me.fycz.fqweb.web

import android.graphics.Bitmap
import fi.iki.elonen.NanoHTTPD
import okio.Pipe
import okio.buffer
import me.fycz.fqweb.utils.JsonUtils
import me.fycz.fqweb.web.controller.DragonController
import xyz.fycz.micat.hook.com.dragon.read.web.ReturnData
import java.io.BufferedWriter
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.OutputStreamWriter
import java.lang.Exception

/**
 * @author fengyue
 * @date 2023/5/29 17:58
 * @description
 */
class HttpServer(port: Int) : NanoHTTPD(port) {
    override fun serve(session: IHTTPSession): Response {
        var returnData: ReturnData? = null
        val ct = ContentType(session.headers["content-type"]).tryUTF8()
        session.headers["content-type"] = ct.contentTypeHeader
        val uri = session.uri
        try {
            if (session.method == Method.GET) {
                val parameters = session.parameters
                returnData = when (uri) {
                    "/search" -> DragonController.search(parameters)
                    "/info" -> DragonController.info(parameters)
                    "/catalog" -> DragonController.catalog(parameters)
                    "/content" -> DragonController.content(parameters)
                    "/reading/bookapi/bookmall/cell/change/v1/" -> DragonController.bookMall(parameters)
                    "/reading/bookapi/new_category/landing/v/" -> DragonController.newCategory(parameters)
                    else -> null
                }
            }/* else if (session.method == Method.POST) {
                val parameters = session.parameters
                val files = HashMap<String, String>()
                session.parseBody(files)
                val postBody = files["postData"]
            }*/
            if (returnData == null) {
                return newFixedLengthResponse("没有数据")
            }
            val response = if (returnData.data is Bitmap) {
                val outputStream = ByteArrayOutputStream()
                (returnData.data as Bitmap).compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                val byteArray = outputStream.toByteArray()
                outputStream.close()
                val inputStream = ByteArrayInputStream(byteArray)
                newFixedLengthResponse(
                    Response.Status.OK,
                    "image/png",
                    inputStream,
                    byteArray.size.toLong()
                )
            } else {
                val data = returnData.data
                if (data is List<*> && data.size > 3000) {
                    val pipe = Pipe(16 * 1024)
                    pipe.sink.buffer().outputStream().use { out ->
                        BufferedWriter(OutputStreamWriter(out, "UTF-8")).use {
                            JsonUtils.toJson(returnData, it)
                        }
                    }
                    newChunkedResponse(
                        Response.Status.OK,
                        "application/json",
                        pipe.source.buffer().inputStream()
                    )
                } else {
                    newFixedLengthResponse(JsonUtils.toJson(returnData))
                }
            }
            response.addHeader("Access-Control-Allow-Methods", "GET, POST")
            response.addHeader("Access-Control-Allow-Origin", session.headers["origin"])
            return response
        } catch (e: Exception) {
            return newFixedLengthResponse(e.stackTraceToString())
        }
    }
}