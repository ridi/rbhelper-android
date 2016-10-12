package com.ridi.books.helper

import java.io.*
import java.nio.ByteBuffer

fun File.deleteTree() {
    if (isDirectory) {
        listFiles()?.let { children ->
            for (child in children) {
                child?.deleteTree()
            }
        }
    }
    delete()
}

fun File.copyTo(dest: File): Boolean {
    try {
        val input = FileInputStream(this)
        val output = FileOutputStream(dest)
        val inChannel = input.channel
        val outChannel = output.channel

        val blockSize = Math.min(512 * 1024 * 1024, inChannel.size())
        var position = 0L
        while (outChannel.transferFrom(inChannel, position, blockSize) > 0) {
            position += blockSize
        }

        inChannel.close()
        input.close()
        outChannel.close()
        output.close()
        return true
    } catch (e: Exception) {
        RBLog.e(javaClass, "file copy error", e)
        return false
    }
}

@Throws(IOException::class)
fun File.getBytes(): ByteArray {
    val input = FileInputStream(this)
    val channel = input.channel

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

    channel.close()
    input.close()
    return bytes
}

fun File.makeNoMediaFile() {
    if (isDirectory.not()) {
        return
    }

    val file = File(this, ".nomedia")
    if (file.exists()) {
        return
    }

    try {
        file.createNewFile()
    } catch (e: IOException) {
        RBLog.e(javaClass, "file creating error", e)
    }
}

@Throws(IOException::class)
fun InputStream.writeStreamToFile(file: File) {
    var output: FileOutputStream? = null
    var exception: IOException? = null
    try {
        output = FileOutputStream(file)
        copyTo(output)
    } catch (e: IOException) {
        exception = e
    } finally {
        closeAndThrowExceptionToReport(output, exception)
    }
}

fun Any.saveToFile(file: File) {
    try {
        file.createNewFile()
        val fileOutput = FileOutputStream(file)
        val objectOutput = ObjectOutputStream(fileOutput)
        objectOutput.writeObject(this)
        objectOutput.flush()
        objectOutput.close()
        fileOutput.close()
    } catch (e: Exception) {
        RBLog.e(javaClass, "writing object to file error", e)
    }
}

@Suppress("UNCHECKED_CAST")
@JvmOverloads
fun <T> File.loadObject(oisClass: Class<out ObjectInputStream> = ObjectInputStream::class.java): T? {
    if (!exists()) {
        return null
    }

    var obj: T? = null

    try {
        val fileInput = FileInputStream(this)
        val objectInput = oisClass.getDeclaredConstructor(InputStream::class.java).newInstance(fileInput)
        obj = objectInput.readObject() as T?
        objectInput.close()
        fileInput.close()
    } catch (e: Exception) {
        RBLog.e(javaClass, "loading object from file error", e)
    }

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
    if (throwable != null) {
        throw throwable
    }
}
