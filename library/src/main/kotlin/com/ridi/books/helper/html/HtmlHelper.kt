package com.ridi.books.helper.html

import android.os.Build
import android.text.Html
import android.text.Spanned

fun fromHtmlCompat(htmlText: String): Spanned {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        Html.fromHtml(htmlText, Html.FROM_HTML_MODE_LEGACY)
    } else {
        @Suppress("DEPRECATION")
        Html.fromHtml(htmlText)
    }
}
