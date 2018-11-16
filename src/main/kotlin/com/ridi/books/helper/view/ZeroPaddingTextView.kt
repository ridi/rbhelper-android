package com.ridi.books.helper.view

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.widget.TextView

class ZeroPaddingTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : TextView(context, attrs) {
    override fun onDraw(canvas: Canvas) {
        canvas.translate(0f, 0f)
        super.onDraw(canvas)
    }
}
