package com.ridi.books.helper.text

/**
 * Normalize the string to MAJOR.MINOR.PATCH format of SemVer.
 * 8 -> 8.0.0
 * 8.4 -> 8.4.0
 * 8.4.2_rc1 -> 8.4.2
 * a.b.c (invalid) -> *
 */
fun String.normalizeAsSemVer(): String {
    val versionIntegers = Regex("^([0-9]+)(?:\\.([0-9]+)(?:\\.([0-9]+))?)?")
            .find(this)?.groupValues ?: return "*"
    fun String.orZeroIfEmpty() = if (isEmpty()) "0" else this
    return versionIntegers.run {
        "${get(1).orZeroIfEmpty()}.${get(2).orZeroIfEmpty()}.${get(3).orZeroIfEmpty()}"
    }
}
