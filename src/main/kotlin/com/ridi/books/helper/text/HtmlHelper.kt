package com.ridi.books.helper.text

import android.os.Build
import android.text.Html
import android.text.Spanned

fun makeSpannedFromHtml(htmlText: String): Spanned {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        Html.fromHtml(htmlText, Html.FROM_HTML_MODE_LEGACY)
    } else {
        @Suppress("DEPRECATION")
        Html.fromHtml(htmlText)
    }
}

fun makeSpannedFromHtmlWithLtGtPreserved(htmlText: String): Spanned {
    // EPUB 본문 텍스트에 사용된 &lt; &gt;를 처리하기 위한 목적으로 만들어졌기 때문에 closing인 경우는 고려하지 않음
    // Close되지 않은 unknown tag에 대해 일괄적으로 closing처리가 되는데 이 경우는 따로 처라히지 않음
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        Html.fromHtml(htmlText, Html.FROM_HTML_MODE_LEGACY, null) { opening, tag, output, _ ->
            if (opening) {
                output.append("<$tag>")
            }
        }
    } else {
        @Suppress("DEPRECATION")
        Html.fromHtml(htmlText, null) { opening, tag, output, _ ->
            if (opening) {
                output.append("<$tag>")
            }
        }
    }
}
