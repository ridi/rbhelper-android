package com.ridi.books.helper.text

import org.junit.Assert
import org.junit.Test

class AlphaNumericComparatorTest {
    @Test
    fun test() {
        val sources = arrayOf("2. bar", "a", "b", "a 1", "a 10",
                "15. baz", "abc", "a 2", "a 3", "a 23", "1. foo")
        val destination = arrayOf("1. foo", "2. bar", "15. baz",
                "a", "a 1", "a 2", "a 3", "a 10", "a 23", "abc", "b")
        Assert.assertArrayEquals(destination, sources.sortedArrayWith(AlphaNumericComparator()))
    }
}
