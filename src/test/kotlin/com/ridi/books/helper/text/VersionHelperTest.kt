package com.ridi.books.helper.text

import org.junit.Assert
import org.junit.Test

class VersionHelperTest {
    @Test
    fun testNormalizeAsSemVer() {
        Assert.assertEquals("8".normalizeAsSemVer(), "8.0.0")
        Assert.assertEquals("8.4".normalizeAsSemVer(), "8.4.0")
        Assert.assertEquals("8.4.12".normalizeAsSemVer(), "8.4.12")
        Assert.assertEquals("13.72512.12".normalizeAsSemVer(), "13.72512.12")
        Assert.assertEquals("8.4.12_rc1".normalizeAsSemVer(), "8.4.12")
        Assert.assertEquals("a".normalizeAsSemVer(), "*")
        Assert.assertEquals("a.8.1".normalizeAsSemVer(), "*")
        Assert.assertEquals("*".normalizeAsSemVer(), "*")
    }
}
