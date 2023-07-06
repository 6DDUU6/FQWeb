package me.fycz.fqweb.constant

import me.fycz.fqweb.utils.GlobalApp
import me.fycz.fqweb.utils.findClass

/**
 * @author fengyue
 * @date 2023/5/30 8:35
 * @description
 */
object Config {

    val settingRecyclerAdapterClz: String by lazy {
        when (versionCode) {
            532 -> "com.dragon.read.base.recyler.c"
            57932 -> "com.dragon.read.recyler.c"
            else -> {
                kotlin.runCatching {
                    "com.dragon.read.recyler.c".findClass(GlobalApp.getClassloader())
                    return@lazy "com.dragon.read.recyler.c"
                }
                "com.dragon.read.base.recyler.c".findClass(GlobalApp.getClassloader())
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
                    "$prefix.k".findClass(GlobalApp.getClassloader())
                    return@lazy "$prefix.k"
                }
                "$prefix.g".findClass(GlobalApp.getClassloader())
                "$prefix.g"
            }
        }
    }

    val settingItemStrFieldName: String by lazy {
        when (versionCode) {
            /*532 -> "i"
            57932 -> "i"*/
            58332 -> "j"
            else -> "i"
        }
    }

    val readerFullRequestClz: String by lazy {
        when (versionCode) {
            /*532 -> "$rpcApiPackage.e"
            57932 -> "$rpcApiPackage.e"*/
            58332 -> "$rpcApiPackage.f"
            else -> "$rpcApiPackage.e"
        }
    }

    val rpcApiPackage: String by lazy {
        val prefix = "com.dragon.read.rpc"
        when (versionCode) {
            532 -> "$prefix.a"
            57932 -> "$prefix.rpc"
            else -> {
                kotlin.runCatching {
                    "$prefix.rpc.a".findClass(GlobalApp.getClassloader())
                    return@lazy "$prefix.rpc"
                }
                "$prefix.a.a".findClass(GlobalApp.getClassloader())
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