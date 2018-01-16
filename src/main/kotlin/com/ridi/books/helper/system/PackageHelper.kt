package com.ridi.books.helper.system

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import com.ridi.books.helper.Log

@JvmOverloads
fun Context.getPackageVersionCode(`package`: String, enabledOnly: Boolean = true): Int {
    try {
        val info = packageManager.getPackageInfo(`package`, 0)
        if (enabledOnly.not() || info.applicationInfo.enabled) {
            if (`package`.startsWith("com.amazon.webview") && info.versionCode / 100000000 < 1) {
                return info.versionName.split(".").let { parts ->
                    if (parts.count() < 5) -1 else parts.drop(2).joinToString("").toInt()
                }
            }
            return info.versionCode
        }
    } catch (e: PackageManager.NameNotFoundException) {
        Log.d("Failed to get $`package` package info", e)
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
        return getPackageVersionCode("com.google.android.webview",
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.N).let { versionCode ->
            if (versionCode > 0) versionCode else
                getPackageVersionCode("com.amazon.webview.chromium")
        }
    }
    return -1
}
