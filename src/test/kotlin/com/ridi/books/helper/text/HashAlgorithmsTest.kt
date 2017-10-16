package com.ridi.books.helper.text

import org.junit.Assert
import org.junit.Test

class HashAlgorithmsTest {
    @Test
    fun testMd5() {
        Assert.assertEquals("c63bae4cbedc34653fad00e8d9da4111", "Must Use".md5())
        Assert.assertEquals("c3c7018f3fdf984d889adeecd93882e9", "Ridibooks".md5())
    }

    @Test
    fun testSha1() {
        Assert.assertEquals("e14c47246f73d243b01f85cf6fcc5bb0c4981c7d", "Must Use".sha1())
        Assert.assertEquals("6a3d4822768f04b0586d4af4af61ec1542d2320a", "Ridibooks".sha1())
    }
}
