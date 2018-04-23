package com.ridi.books.helper.text

import java.io.File

class FilePathComparator : Comparator<File> {
    private val alphaNumericComparator = AlphaNumericComparator()

    override fun compare(lhs: File, rhs: File) = alphaNumericComparator.compare(lhs.path, rhs.path)
}
