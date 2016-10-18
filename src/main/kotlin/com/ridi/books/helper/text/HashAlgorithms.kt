package com.ridi.books.helper.text

import com.ridi.books.helper.Log
import java.security.MessageDigest

object HashAlgorithms {
    @JvmStatic
    fun md5(src: String): String {
        return hash(src, "MD5")
    }

    @JvmStatic
    fun sha1(src: String): String {
        return hash(src, "SHA-1")
    }

    private fun hash(src: String, algorithm: String): String {
        try {
            val digest = MessageDigest.getInstance(algorithm)
            digest.update(src.toByteArray())
            val messageDigest = digest.digest()

            var hexString = ""
            for (i in messageDigest.indices) {
                hexString += String.format("%02x", messageDigest[i])
            }
            return hexString
        } catch (e: Exception) {
            Log.e(HashAlgorithms::class.java, e)
            throw e
        }
    }
}
