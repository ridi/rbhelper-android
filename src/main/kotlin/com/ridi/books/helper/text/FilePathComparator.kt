package com.ridi.books.helper.text

import java.io.File
import java.util.*

/**
 * Created by kering on 2016. 11. 30..
 */
class FilePathComparator : Comparator<File> {
    private val alphaNumericComparator = AlphaNumericComparator()

    override fun compare(lhs: File, rhs: File) = alphaNumericComparator.compare(lhs.path, rhs.path)
}
