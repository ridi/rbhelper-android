package com.ridi.books.helper.text

fun Long.elapsedTimeString(tooMuchElapsed: String): String {
    var elapsed = System.currentTimeMillis() - this
    val second = 1000
    val minute = second * 60
    val hour = minute * 60
    val day = (hour * 24).toLong()
    val week = day * 7
    val suffix: String

    if (elapsed / week > 3) {
        return tooMuchElapsed
    } else if (elapsed / week > 0) {
        suffix = "주 전"
        elapsed /= week
    } else if (elapsed / day > 0) {
        suffix = "일 전"
        elapsed /= day
    } else if (elapsed / hour > 0) {
        suffix = "시간 전"
        elapsed /= hour
    } else if (elapsed / minute > 0) {
        suffix = "분 전"
        elapsed /= minute
    } else if (elapsed / second > 10) {
        suffix = "초 전"
        elapsed /= second
    } else {
        return "방금 전"
    }

    return "$elapsed$suffix"
}
