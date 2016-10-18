package com.ridi.books.helper.text

import com.ridi.books.helper.Log
import java.security.MessageDigest

private fun String.hash(algorithm: String): String {
    try {
        val digest = MessageDigest.getInstance(algorithm)
        digest.update(toByteArray())
        val messageDigest = digest.digest()

        var hexString = ""
        for (i in messageDigest.indices) {
            hexString += String.format("%02x", messageDigest[i])
        }
        return hexString
    } catch (e: Exception) {
        Log.e(javaClass, e)
        throw e
    }
}

fun String.md5() = hash("MD5")

fun String.sha1() = hash("SHA-1")
