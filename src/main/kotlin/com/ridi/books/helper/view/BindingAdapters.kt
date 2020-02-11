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
                @Px strokeSize: Int = 0,
                @ColorInt backgroundColor: Int?,
                @Px radius: Int?
        ) {
            view.apply {
                val gradientDrawable = GradientDrawable()
                radius?.let { gradientDrawable.cornerRadius = dip(radius) }
                backgroundColor?.let { gradientDrawable.setColor(backgroundColor) }
                strokeColor?.let { gradientDrawable.setStroke(dip(strokeSize).toInt(), strokeColor) }
                background = gradientDrawable
            }
        }

        @BindingAdapter("htmlText")
        @JvmStatic
        fun setHtmlText(textView: TextView, htmlText: String) {
            textView.text = makeSpannedFromHtml(htmlText)
        }
    }
}
