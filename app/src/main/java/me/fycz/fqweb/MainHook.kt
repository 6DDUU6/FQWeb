package me.fycz.fqweb

import android.app.AlertDialog
import android.app.Application
import android.content.Context
import android.graphics.Color
import android.text.InputFilter
import android.text.InputType
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.callbacks.XC_LoadPackage
import me.fycz.fqweb.constant.Config
import me.fycz.fqweb.utils.GlobalApp
import me.fycz.fqweb.utils.SPUtils
import me.fycz.fqweb.utils.callMethod
import me.fycz.fqweb.utils.findClass
import me.fycz.fqweb.utils.getObjectField
import me.fycz.fqweb.utils.hookAfterMethod
import me.fycz.fqweb.utils.invokeOriginalMethod
import me.fycz.fqweb.utils.log
import me.fycz.fqweb.utils.new
import me.fycz.fqweb.utils.replaceMethod
import me.fycz.fqweb.utils.setObjectField
import me.fycz.fqweb.web.HttpServer
import java.util.LinkedList


/**
 * @author fengyue
 * @date 2022/5/3 12:59
 */
class MainHook : IXposedHookLoadPackage {

    companion object {
        lateinit var httpServer: HttpServer
    }

    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        if (lpparam.packageName == "com.dragon.read") {
            GlobalApp.initClassLoader(lpparam.classLoader)
            "com.dragon.read.app.MainApplication".hookAfterMethod(
                lpparam.classLoader,
                "onCreate"
            ) {
                val app = it.thisObject as Application
                GlobalApp.application = app
                log("versionCode = ${Config.versionCode}")
                SPUtils.init(app)
                httpServer = HttpServer(SPUtils.getInt("port", 9999))
                hookSetting(lpparam.classLoader)
                if (!httpServer.wasStarted() && SPUtils.getBoolean(
                        "autoStart",
                        false
                    )
                ) httpServer.start()
            }
        }
    }

    private fun hookSetting(classLoader: ClassLoader) {
        var adapter: Any? = null
        "com.dragon.read.component.biz.impl.mine.settings.SettingsActivity"
            .hookAfterMethod(
                classLoader,
                "a",
                Config.settingRecyclerAdapterClz.findClass(classLoader)
            ) {
                adapter = it.thisObject.getObjectField("b")
                val list = it.result as LinkedList<Any>
                if (list[0].getObjectField("e") != "Web服务") {
                    val context = it.thisObject as Context
                    val setting =
                        Config.settingItemQSNClz.findClass(classLoader)
                            .new(context)
                    setting.setObjectField("e", "Web服务")
                    setting.setObjectField(
                        "i",
                        if (httpServer.isAlive) "已开启(端口：${
                            SPUtils.getInt(
                                "port",
                                9999
                            )
                        })" else "未开启"
                    )
                    list.add(0, setting)
                }
            }
        "${Config.settingItemQSNClz}$1"
            .replaceMethod(
                classLoader, "a",
                View::class.java,
                "com.dragon.read.pages.mine.settings.e".findClass(classLoader),
                Int::class.java
            ) {
                val context = (it.args[0] as View).context
                if (it.args[1].getObjectField("e") == "Web服务") {
                    dialog(context, adapter, it.args[1])
                } else {
                    it.invokeOriginalMethod()
                }
            }
    }

    fun dialog(context: Context, adapter: Any?, settingView: Any) {
        val textColor = Color.parseColor("#060606")

        val layout_root = LinearLayout(context)
        layout_root.setPadding(
            dp2px(context, 10F),
            dp2px(context, 10F),
            dp2px(context, 10F),
            dp2px(context, 10F)
        )
        val linearlayout_0 = LinearLayout(context)
        val layoutParams_0 = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        linearlayout_0.orientation = LinearLayout.VERTICAL
        val linearlayout_1 = LinearLayout(context)
        val layoutParams_1 = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        linearlayout_1.setPadding(
            dp2px(context, 10F),
            dp2px(context, 10F),
            dp2px(context, 10F),
            dp2px(context, 10F)
        )
        linearlayout_1.orientation = LinearLayout.HORIZONTAL


        val textview_2 = TextView(context)
        val layoutParams_2 = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        textview_2.text = "服务端口："
        textview_2.setTextColor(textColor)
        textview_2.textSize = 16F
        linearlayout_1.addView(textview_2, layoutParams_2)
        val et_port = EditText(context).apply {
            inputType = InputType.TYPE_CLASS_NUMBER
            filters = arrayOf(InputFilter.LengthFilter(5))
        }
        val layoutParams_3 = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        et_port.hint = "请输入1000-65535之间的值"
        et_port.setText(SPUtils.getInt("port", 9999).toString())
        et_port.setTextColor(textColor)
        et_port.textSize = 16F
        linearlayout_1.addView(et_port, layoutParams_3)

        linearlayout_0.addView(linearlayout_1, layoutParams_1)
        val linearlayout_4 = LinearLayout(context)
        val layoutParams_4 = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        linearlayout_4.setPadding(
            dp2px(context, 10F),
            dp2px(context, 10F),
            dp2px(context, 10F),
            dp2px(context, 10F)
        )
        linearlayout_4.orientation = LinearLayout.HORIZONTAL
        val textview_5 = TextView(context)
        val layoutParams_5 = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        textview_5.text = "随番茄自动启动服务："
        textview_5.setTextColor(textColor)
        textview_5.textSize = 16F
        linearlayout_4.addView(textview_5, layoutParams_5)
        val s_auto_start = Switch(context).apply {
            isChecked = SPUtils.getBoolean("autoStart", false)
        }
        val layoutParams_6 = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        linearlayout_4.addView(s_auto_start, layoutParams_6)
        linearlayout_0.addView(linearlayout_4, layoutParams_4)
        val linearlayout_7 = LinearLayout(context)
        val layoutParams_7 = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        linearlayout_7.setPadding(
            dp2px(context, 10F),
            dp2px(context, 10F),
            dp2px(context, 10F),
            dp2px(context, 10F)
        )
        linearlayout_7.orientation = LinearLayout.HORIZONTAL
        val textview_8 = TextView(context)
        val layoutParams_8 = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        textview_8.text = "开启服务："
        textview_8.setTextColor(textColor)
        textview_8.textSize = 16F
        linearlayout_7.addView(textview_8, layoutParams_8)
        val s_enable = Switch(context).apply {
            isChecked = httpServer.isAlive
        }
        val layoutParams_9 = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        linearlayout_7.addView(s_enable, layoutParams_9)
        linearlayout_0.addView(linearlayout_7, layoutParams_7)
        layout_root.addView(linearlayout_0, layoutParams_0)

        AlertDialog.Builder(context)
            .setTitle("Web服务")
            .setView(layout_root)
            .setNegativeButton("取消", null)
            .setPositiveButton("保存设置") { dialog, _ ->
                SPUtils.putBoolean("autoStart", s_auto_start.isChecked)
                val port = et_port.text.toString().toInt()
                if (port !in 1000..65535) {
                    Toast.makeText(context, "端口只能在1000-65535之间", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    SPUtils.putInt("port", port)
                }
                if (s_enable.isChecked) {
                    restartServe()
                    settingView.setObjectField("i", "已开启(端口：$port)")
                    adapter?.callMethod("notifyItemChanged", 0)
                    //Toast.makeText(context, "服务已开启，运行在${port}端口", Toast.LENGTH_SHORT).show()
                } else {
                    httpServer.stop()
                    settingView.setObjectField("i", "未开启")
                    adapter?.callMethod("notifyItemChanged", 0)
                    //Toast.makeText(context, "服务已关闭", Toast.LENGTH_SHORT).show()
                }
            }.create().show()
    }

    private fun restartServe(port: Int = SPUtils.getInt("port", 9999)) {
        if (httpServer.isAlive) {
            if (httpServer.listeningPort != port) {
                httpServer.stop()
                httpServer = HttpServer(port)
                httpServer.start()
            }
        } else {
            httpServer = HttpServer(port)
            httpServer.start()
        }
    }

    private fun dp2px(context: Context, dipValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dipValue * scale + 0.5f).toInt()
    }
}