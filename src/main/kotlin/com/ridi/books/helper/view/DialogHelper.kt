package com.ridi.books.helper.view

import android.app.Dialog
import com.ridi.books.helper.Log

fun Dialog?.dismissSafely() {
    if (this?.isShowing == true) {
        try {
            dismiss()
        } catch (e: Exception) {
            Log.d(this.javaClass, e)
        }
    }
}
