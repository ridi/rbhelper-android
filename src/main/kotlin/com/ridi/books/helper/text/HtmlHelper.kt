package com.ridi.books.helper.text

import android.os.Build
import android.text.Editable
import android.text.Html
import android.text.Spanned
import org.xml.sax.XMLReader

fun makeSpannedFromHtml(htmlText: String): Spanned {
    return makeSpannedFromHtml(htmlText, null)
}

fun makeSpannedFromHtml(
    htmlText: String,
    tagHandler: ((opening: Boolean, tag: String, output: Editable, xmlReader: XMLReader) -> Unit)?
): Spanned {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        Html.fromHtml(htmlText, Html.FROM_HTML_MODE_LEGACY, null, tagHandler)
    } else {
        @Suppress("DEPRECATION")
        Html.fromHtml(htmlText, null, tagHandler)
    }
}
