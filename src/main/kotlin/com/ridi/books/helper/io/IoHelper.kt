package com.ridi.books.helper.io

import com.ridi.books.helper.Log
import java.io.Closeable
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.nio.ByteBuffer

private const val IOHELPER_TAG = "IoHelper"

fun File.copyTo(dest: File): Boolean {
    runCatching {
        copyTo(dest, overwrite = true)
    }.let {
        if (it.isSuccess) return true

        Log.e(IOHELPER_TAG, "file copy error", it.exceptionOrNull())
        return false
    }
}

fun File.moveTo(dest: File): Boolean {
    runCatching {
        copyTo(dest, overwrite = true)
        delete()
    }.let {
        if (it.isSuccess) return true

        Log.e(IOHELPER_TAG, "file move error", it.exceptionOrNull())
        return false
    }
}

@Throws(IOException::class)
fun File.getBytes(): ByteArray {
    this.inputStream().use { input ->
        input.channel.use { channel ->
            val length = length()
            // Create the byte array to hold the data
            val bytes = ByteArray(length.toInt())
            val buffer = ByteBuffer.wrap(bytes)

            var offset = 0L
            var numRead: Int
            do {
                numRead = channel.read(buffer, offset)
                offset += numRead
            } while (offset < length && numRead >= 0)

            return bytes
        }
    }
}

fun File.makeNoMediaFile() {
    if (isDirectory.not()) return

    val file = File(this, ".nomedia")
    if (file.exists()) return

    try {
        file.createNewFile()
    } catch (e: IOException) {
        Log.e(IOHELPER_TAG, "file creating error", e)
    }
}

@Throws(IOException::class)
fun InputStream.writeStreamToFile(file: File) {
    runCatching {
        file.outputStream().use { output ->
            copyTo(output)
        }
    }.onFailure { throw it }
}

fun Any.saveToFile(file: File) {
    runCatching {
        file.createNewFile()
        file.outputStream().use { fileOutput ->
            ObjectOutputStream(fileOutput).use { objectOutput ->
                objectOutput.writeObject(this)
                objectOutput.flush()
            }
        }
    }.onFailure { Log.e(IOHELPER_TAG, "writing object to file error", it) }
}

@Suppress("UNCHECKED_CAST")
fun <T> File.loadObject(): T? {
    if (exists().not()) return null

    var obj: T? = null

    runCatching {
        this.inputStream().use { fileInput ->
            ObjectInputStream(fileInput).use { objectInput ->
                obj = objectInput.readObject() as T?
            }
        }
    }.onFailure { Log.e(IOHELPER_TAG, "loading object from file error", it) }

    return obj
}

@Throws(IOException::class)
fun closeAndThrowExceptionToReport(closeable: Closeable?, primaryException: IOException?) {
    var throwable = primaryException
    if (closeable != null) {
        try {
            closeable.close()
        } catch (e: IOException) {
            throwable = primaryException ?: e
        }
    }
    throwable?.let {
        throw it
    }
}
