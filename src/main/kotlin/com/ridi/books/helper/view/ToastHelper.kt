package com.ridi.books.helper.view

import android.view.Gravity
import android.widget.Toast

@JvmOverloads
fun Toast?.showAtCenter(xOffset: Int = 0, yOffset: Int = 0) {
    this?.setGravity(Gravity.CENTER, xOffset, yOffset)
    this?.show()
}
