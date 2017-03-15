package com.ridi.books.helper.text

import org.junit.Assert
import org.junit.Test

class VersionHelperTest {
    @Test
    fun testNormalizeAsSemVer() {
        Assert.assertEquals("8.0.0", "8".normalizeAsSemVer())
        Assert.assertEquals("8.4.0", "8.4".normalizeAsSemVer())
        Assert.assertEquals("8.4.12", "8.4.12".normalizeAsSemVer())
        Assert.assertEquals("13.72512.12", "13.72512.12".normalizeAsSemVer())
        Assert.assertEquals("8.4.12", "8.4.12_rc1".normalizeAsSemVer())
        Assert.assertEquals("*", "a".normalizeAsSemVer())
        Assert.assertEquals("*", "a.8.1".normalizeAsSemVer())
        Assert.assertEquals("*", "*".normalizeAsSemVer())
    }
}
