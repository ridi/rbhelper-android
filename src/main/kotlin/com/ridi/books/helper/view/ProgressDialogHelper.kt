package com.ridi.books.helper.view

import android.app.ProgressDialog
import com.ridi.books.helper.Log

/**
 * Created by kering on 2016. 10. 15..
 */

fun ProgressDialog?.dismissSafely() {
    if (this?.isShowing ?: false) {
        try {
            this!!.dismiss()
        } catch (e: Exception) {
            Log.e(this!!.javaClass, e)
        }
    }
}
