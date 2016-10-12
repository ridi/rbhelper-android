package com.ridi.books.helper

import android.util.Log

object RidiLog {
    @JvmStatic
    fun e(tag: String, e: Throwable?) = e(tag, "", e)

    @JvmOverloads
    @JvmStatic
    fun e(tag: String, message: String, e: Throwable? = null) = Log.e(tag, message, e)

    @JvmStatic
    fun e(clazz: Class<*>, e: Throwable?) = e(clazz, "", e)

    @JvmOverloads
    @JvmStatic
    fun e(clazz: Class<*>, message: String, e: Throwable? = null) = e(clazz.simpleName, message, e)

    @JvmStatic
    fun w(tag: String, e: Throwable?) = w(tag, "", e)

    @JvmOverloads
    @JvmStatic
    fun w(tag: String, message: String, e: Throwable? = null) = Log.w(tag, message, e)

    @JvmStatic
    fun w(clazz: Class<*>, e: Throwable?) = w(clazz, "", e)

    @JvmOverloads
    @JvmStatic
    fun w(clazz: Class<*>, message: String, e: Throwable? = null) = w(clazz.simpleName, message, e)

    @JvmStatic
    fun d(tag: String, e: Throwable?) = d(tag, "", e)

    @JvmOverloads
    @JvmStatic
    fun d(tag: String, message: String, e: Throwable? = null) = Log.d(tag, message, e)

    @JvmStatic
    fun d(clazz: Class<*>, e: Throwable?) = d(clazz, "", e)

    @JvmOverloads
    @JvmStatic
    fun d(clazz: Class<*>, message: String, e: Throwable? = null) = d(clazz.simpleName, message, e)

    @JvmStatic
    fun d(message: String) = d("ridibug", message)

    @JvmStatic
    fun v(tag: String, e: Throwable?) = v(tag, "", e)

    @JvmOverloads
    @JvmStatic
    fun v(tag: String, message: String, e: Throwable? = null) = Log.v(tag, message, e)

    @JvmStatic
    fun v(clazz: Class<*>, e: Throwable?) = v(clazz, "", e)

    @JvmOverloads
    @JvmStatic
    fun v(clazz: Class<*>, message: String, e: Throwable? = null) = v(clazz.simpleName, message, e)

    @JvmStatic
    fun i(tag: String, e: Throwable?) = i(tag, "", e)

    @JvmOverloads
    @JvmStatic
    fun i(tag: String, message: String, e: Throwable? = null) = Log.i(tag, message, e)

    @JvmStatic
    fun i(clazz: Class<*>, e: Throwable?) = i(clazz, "", e)

    @JvmOverloads
    @JvmStatic
    fun i(clazz: Class<*>, message: String, e: Throwable? = null) = i(clazz.simpleName, message, e)
}
