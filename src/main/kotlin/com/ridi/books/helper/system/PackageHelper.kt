package com.ridi.books.helper.system

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import com.ridi.books.helper.Log

@JvmOverloads
fun Context.getPackageVersionCode(name: String, enabledOnly: Boolean = true): Int {
    try {
        val info = packageManager.getPackageInfo(name, 0)
        if (enabledOnly.not() || info.applicationInfo.enabled) {
            return info.versionCode
        }
    } catch (e: PackageManager.NameNotFoundException) {
        Log.d(javaClass, "Failed to get $name package info", e)
    }
    return -1
}

fun Context.getSystemWebViewVersionCode(): Int {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        val versionCode = getPackageVersionCode("com.android.chrome")
        if (versionCode > 0) {
            return versionCode
        }
    }
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        val versionCode = getPackageVersionCode("com.google.android.webview",
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
        if (versionCode > 0) {
            return versionCode
        } else {
            val packageName = "com.amazon.webview.chromium"
            try {
                val info = packageManager.getPackageInfo(packageName, 0)
                return if (info.versionCode >= 100000000) {
                    info.versionCode
                } else {
                    info.versionName.split(".").let { parts ->
                        if (parts.count() != 5) {
                            -1
                        } else {
                            parts.drop(2).joinToString("").take(9).toInt()
                        }
                    }
                }
            } catch (e: PackageManager.NameNotFoundException) {
                Log.d(javaClass, "Failed to get $packageName package info", e)
            }
        }
    }
    return -1
}
