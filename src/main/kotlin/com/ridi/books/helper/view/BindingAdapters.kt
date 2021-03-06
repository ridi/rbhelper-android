package com.ridi.books.helper.view

import android.graphics.drawable.GradientDrawable
import android.view.View
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.Px
import androidx.databinding.BindingAdapter
import com.ridi.books.helper.text.makeSpannedFromHtml

class BindingAdapters {

    companion object {
        @BindingAdapter(value = ["strokeColor", "strokeSize", "backgroundColor", "radius"], requireAll = false)
        @JvmStatic
        fun setCustomBackground(
            view: View,
            @ColorInt strokeColor: Int?,
            @Px strokeSize: Int?,
            @ColorInt backgroundColor: Int?,
            @Px radius: Int?
        ) {
            view.apply {
                val prevPaddingLeft = paddingLeft
                val prevPaddingRight = paddingRight
                val prevPaddingTop = paddingTop
                val prevPaddingBottom = paddingBottom

                val gradientDrawable = GradientDrawable()
                radius?.let { gradientDrawable.cornerRadius = dip(radius) }
                backgroundColor?.let { gradientDrawable.setColor(backgroundColor) }
                strokeColor?.let { gradientDrawable.setStroke(dip(strokeSize ?: 1).toInt(), strokeColor) }
                background = gradientDrawable
                setPadding(prevPaddingLeft, prevPaddingTop, prevPaddingRight, prevPaddingBottom)
            }
        }

        @BindingAdapter("htmlText")
        @JvmStatic
        fun setHtmlText(textView: TextView, htmlText: String) {
            textView.text = makeSpannedFromHtml(htmlText)
        }
    }
}
