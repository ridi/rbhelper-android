package com.ridi.books.helper.system

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo

private fun Context.getActiveNetworkInfo(): NetworkInfo? {
    val manager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    return manager.activeNetworkInfo
}

fun Context.isNetworkAvailable() = getActiveNetworkInfo()?.isConnected ?: false

fun Context.isMobileNetworkConnected() = getActiveNetworkInfo()?.let { networkInfo ->
    networkInfo.type == ConnectivityManager.TYPE_MOBILE && networkInfo.isConnected
} ?: false
