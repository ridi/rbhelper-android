package com.ridi.books.helper.system

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import com.ridi.books.helper.Log

/**
 * Created by kering on 2016. 10. 18..
 */
fun Context.getPackageVersionCode(`package`: String): Int {
    try {
        val info = packageManager.getPackageInfo(`package`, 0)
        if (info.applicationInfo.enabled) {
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
        return getPackageVersionCode("com.google.android.webview")
    }
    return -1
}
