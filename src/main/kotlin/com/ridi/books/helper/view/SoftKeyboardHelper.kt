package com.ridi.books.helper.view

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

private val Context.inputMethodManager: InputMethodManager
    get() = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

@JvmOverloads
fun View.showSoftKeyboard(flags: Int = InputMethodManager.SHOW_IMPLICIT) =
        context.inputMethodManager.showSoftInput(this, flags)

@JvmOverloads
fun View.hideSoftKeyboard(flags: Int = 0) =
        context.inputMethodManager.hideSoftInputFromWindow(windowToken, flags)

@JvmOverloads
fun Context.toggleSoftKeyboard(showFlags: Int = 0, hideFlags: Int = 0) {
    inputMethodManager.toggleSoftInput(showFlags, hideFlags)
}
