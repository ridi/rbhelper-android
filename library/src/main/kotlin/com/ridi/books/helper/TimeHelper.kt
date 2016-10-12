package com.ridi.books.helper

import java.util.Calendar

fun Long.elapsedTimeString(): String {
    var elapsed = System.currentTimeMillis() - this
    val second = 1000
    val minute = second * 60
    val hour = minute * 60
    val day = (hour * 24).toLong()
    val week = day * 7
    val suffix: String

    if (elapsed / week > 3) {
        val cal = Calendar.getInstance()
        cal.timeInMillis = this
        return "${cal.get(Calendar.YEAR)}.${cal.get(Calendar.MONDAY) + 1}.${cal.get(Calendar.DAY_OF_MONTH)}"
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
