package com.ridi.books.helper.view

import android.app.ProgressDialog
import com.ridi.books.helper.Log

fun ProgressDialog?.dismissSafely() {
    if (this?.isShowing == true) {
        try {
            dismiss()
        } catch (e: Exception) {
            Log.d(this.javaClass, e)
        }
    }
}
