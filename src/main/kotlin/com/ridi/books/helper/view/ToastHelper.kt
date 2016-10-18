package com.ridi.books.helper.view

import android.view.Gravity
import android.widget.Toast

/**
 * Created by kering on 2016. 10. 12..
 */
@JvmOverloads
fun Toast?.showAtCenter(xOffset: Int = 0, yOffset: Int = 0) {
    this?.setGravity(Gravity.CENTER, xOffset, yOffset)
    this?.show()
}
