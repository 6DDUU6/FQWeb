package me.fycz.fqweb.web.api.annotation

/**
 * @author fengyue
 * @date 2023/5/30 10:23
 * @description
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class DragonGet(val value: String = "")