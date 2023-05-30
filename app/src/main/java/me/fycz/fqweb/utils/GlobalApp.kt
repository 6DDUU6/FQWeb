package me.fycz.fqweb.utils

import android.app.Application


/**
 * @author fengyue
 * @date 2023/5/28 16:55
 * @description
 */
object GlobalApp {

    private var classLoader: ClassLoader? = null

    var application: Application? = null
        get()  {
            if (field == null) {
                kotlin.runCatching {
                    application = "android.app.ActivityThread".findClass(classLoader)
                        .callStaticMethod("currentApplication") as Application
                }
            }
            return field
        }

    fun initClassLoader(classLoader: ClassLoader) {
        GlobalApp.classLoader = classLoader
    }

    fun hasInit(): Boolean{
        return classLoader != null
    }

    fun getClassloader(): ClassLoader {
        return classLoader!!
    }

}