package com.ridi.books.helper.system

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo

/**
 * Created by kering on 2016. 10. 14..
 */

object NetworkHelper {
    private fun Context.getActiveNetworkInfo(): NetworkInfo? {
        val manager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return manager.activeNetworkInfo
    }

    @JvmStatic
    fun isNetworkAvailable(context: Context) =
            context.getActiveNetworkInfo()?.isConnectedOrConnecting ?: false

    @JvmStatic
    fun isMobileNetworkConnected(context: Context) =
            context.getActiveNetworkInfo()?.let { networkInfo ->
                networkInfo.type == ConnectivityManager.TYPE_MOBILE && networkInfo.isConnectedOrConnecting
            } ?: false
}
