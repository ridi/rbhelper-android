package com.ridi.books.helper

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.JsonParser
import com.google.gson.JsonSyntaxException
import kotlin.reflect.KProperty

abstract class Preferences {
    abstract val preferences: SharedPreferences

    abstract inner class Delegate(protected val key: String)

    open inner class BooleanDelegate(
        key: String,
        private val defaultValue: Boolean = false
    ) : Delegate(key) {
        open operator fun getValue(thisRef: Any?, property: KProperty<*>): Boolean =
                preferences.getBoolean(key, defaultValue)

        open operator fun setValue(thisRef: Any?, property: KProperty<*>, value: Boolean) =
                preferences.edit().putBoolean(key, value).apply()
    }

    open inner class IntDelegate(
        key: String,
        private val defaultValue: Int = 0
    ) : Delegate(key) {
        open operator fun getValue(thisRef: Any?, property: KProperty<*>): Int =
                preferences.getInt(key, defaultValue)

        open operator fun setValue(thisRef: Any?, property: KProperty<*>, value: Int) =
                preferences.edit().putInt(key, value).apply()
    }

    open inner class LongDelegate(
        key: String,
        private val defaultValue: Long = 0L
    ) : Delegate(key) {
        open operator fun getValue(thisRef: Any?, property: KProperty<*>): Long =
                preferences.getLong(key, defaultValue)

        open operator fun setValue(thisRef: Any?, property: KProperty<*>, value: Long) =
                preferences.edit().putLong(key, value).apply()
    }

    inner class StringDelegate(
        key: String,
        private val defaultValue: String? = null
    ) : Delegate(key) {
        operator fun getValue(thisRef: Any?, property: KProperty<*>): String? =
                preferences.getString(key, defaultValue)

        operator fun setValue(thisRef: Any?, property: KProperty<*>, value: String?) =
                preferences.edit().putString(key, value).apply()
    }

    inner class JsonDelegate<T>(
        key: String,
        private val clazz: Class<T>,
        private val defaultValue: T? = null
    ) : Delegate(key) {
        private val gson by lazy { Gson() }
        private val jsonParser by lazy { JsonParser() }

        operator fun getValue(thisRef: Any?, property: KProperty<*>): T? =
                preferences.getString(key, null)?.let { jsonString ->
                    try {
                        gson.fromJson(jsonParser.parse(jsonString), clazz)
                    } catch (e: JsonSyntaxException) {
                        Log.e(javaClass, e)
                        defaultValue
                    }
                } ?: defaultValue

        operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T?) =
                preferences.edit()
                        .putString(key, value?.let { gson.toJson(it).toString() })
                        .apply()
    }
}
