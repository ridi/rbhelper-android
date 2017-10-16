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
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
    }
    return -1
}
