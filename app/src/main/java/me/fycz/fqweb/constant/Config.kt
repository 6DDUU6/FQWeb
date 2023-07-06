package me.fycz.fqweb.constant

import de.robv.android.xposed.XposedHelpers
import me.fycz.fqweb.utils.GlobalApp
import me.fycz.fqweb.utils.findClass

/**
 * @author fengyue
 * @date 2023/5/30 8:35
 * @description
 */
object Config {

    private val dragonClassloader by lazy { GlobalApp.getClassloader() }

    val settingRecyclerAdapterClz: String by lazy {
        when (versionCode) {
            532 -> "com.dragon.read.base.recyler.c"
            57932 -> "com.dragon.read.recyler.c"
            else -> {
                kotlin.runCatching {
                    "com.dragon.read.recyler.c".findClass(dragonClassloader)
                    return@lazy "com.dragon.read.recyler.c"
                }
                "com.dragon.read.base.recyler.c"
            }
        }
    }

    val settingItemQSNClz: String by lazy {
        val prefix = "com.dragon.read.component.biz.impl.mine.settings.a"
        when (versionCode) {
            532 -> "$prefix.g"
            57932 -> "$prefix.k"
            else -> {
                kotlin.runCatching {
                    "$prefix.k".findClass(dragonClassloader)
                    return@lazy "$prefix.k"
                }
                "$prefix.g"
            }
        }
    }

    val settingItemStrFieldName: String by lazy {
        when (versionCode) {
            532 -> "i"
            57932 -> "i"
            58332 -> "j"
            else -> {
                val settingItemClz =
                    "com.dragon.read.pages.mine.settings.e".findClass(dragonClassloader)
                val iField = XposedHelpers.findField(settingItemClz, "i")
                if (iField.type == CharSequence::class.java) {
                    "i"
                } else {
                    "j"
                }
            }
        }
    }

    val readerFullRequestClz: String by lazy {
        when (versionCode) {
            532 -> "$rpcApiPackage.e"
            57932 -> "$rpcApiPackage.e"
            58332 -> "$rpcApiPackage.f"
            else -> {
                val FullRequest =
                    "$rpcModelPackage.FullRequest".findClass(dragonClassloader)
                kotlin.runCatching {
                    XposedHelpers.findMethodExact(
                        "$rpcApiPackage.e",
                        dragonClassloader,
                        "a",
                        FullRequest
                    )
                    "$rpcApiPackage.e"
                }
                "$rpcApiPackage.f"
            }
        }
    }

    val rpcApiPackage: String by lazy {
        val prefix = "com.dragon.read.rpc"
        when (versionCode) {
            532 -> "$prefix.a"
            57932 -> "$prefix.rpc"
            else -> {
                kotlin.runCatching {
                    "$prefix.rpc.a".findClass(dragonClassloader)
                    return@lazy "$prefix.rpc"
                }
                "$prefix.a.a".findClass(dragonClassloader)
                "$prefix.a"
            }
        }
    }

    const val rpcModelPackage = "com.dragon.read.rpc.model"

    val versionCode: Int by lazy {
        try {
            val manager =
                GlobalApp.application!!.packageManager
            val info = manager.getPackageInfo(GlobalApp.application!!.packageName, 0)
            info.versionCode
        } catch (e: Exception) {
            e.printStackTrace()
            0
        }
    }
}