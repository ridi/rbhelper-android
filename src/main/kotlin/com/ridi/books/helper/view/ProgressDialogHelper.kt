package com.ridi.books.helper.view

import android.app.ProgressDialog
import com.ridi.books.helper.Log

fun ProgressDialog?.dismissSafely() {
    if (this?.isShowing ?: false) {
        try {
            this!!.dismiss()
        } catch (e: Exception) {
            Log.d(this!!.javaClass, e)
        }
    }
}
