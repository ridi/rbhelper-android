package com.ridi.books.helper.system

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo

/**
 * Created by kering on 2016. 10. 14..
 */

private fun Context.getActiveNetworkInfo(): NetworkInfo? {
    val manager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    return manager.activeNetworkInfo
}

fun Context.isNetworkAvailable() = getActiveNetworkInfo()?.isConnectedOrConnecting ?: false

fun Context.isMobileNetworkConnected() = getActiveNetworkInfo()?.let { networkInfo ->
            networkInfo.type == ConnectivityManager.TYPE_MOBILE && networkInfo.isConnectedOrConnecting
        } ?: false
