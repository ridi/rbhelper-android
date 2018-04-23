package com.ridi.books.helper.io

import com.ridi.books.helper.Log
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream
import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.nio.charset.MalformedInputException

object ZipHelper {
    private const val BUFFER_SIZE = 8192
    private const val PROGRESS_UPDATE_STEP_SIZE = BUFFER_SIZE / 16

    interface Listener {
        fun onProgress(unzippedBytes: Long)
    }

    interface Encryptor {
        @Throws(EncryptionUnnecessaryException::class, EncryptionFailedException::class)
        fun encrypt(zipArchiveInputStream: ZipArchiveInputStream, outputStream: OutputStream, fileName: String)
    }

    @JvmStatic
    @JvmOverloads
    fun unzip(
        zipFile: File,
        destDir: File,
        deleteZip: Boolean,
        listener: Listener? = null,
        encodings: List<String> = listOf("UTF8"),
        overwrite: Boolean = true
    ): Boolean {
        if (zipFile.exists().not()) {
            return false
        }
        return try {
            encodings.any {
                unzip(BufferedInputStream(FileInputStream(zipFile)),
                        destDir, listener, null, it, overwrite)
            }
        } catch (e: Exception) {
            Log.e(javaClass, "error while unzip", e)
            false
        } finally {
            if (deleteZip) {
                zipFile.delete()
            }
        }
    }

    @JvmStatic
    fun unzipEncryptIfNeeded(inputStream: InputStream, destDir: File, listener: Listener?, encryptor: Encryptor?) =
        unzip(inputStream, destDir, listener, encryptor)

    @JvmStatic
    fun unzipSelectedFile(inputStream: InputStream, destDir: File, selectedFileName: String): Boolean =
        unzip(inputStream, destDir, selectedFileName = selectedFileName)

    @JvmStatic
    @JvmOverloads
    fun unzip(
        inputStream: InputStream,
        destDir: File,
        listener: Listener? = null,
        encryptor: Encryptor? = null,
        encoding: String = "UTF8",
        overwrite: Boolean = true,
        selectedFileName: String? = null
    ): Boolean {
        destDir.mkdirs()

        val zipInputStream = ZipArchiveInputStream(inputStream, encoding, true, true)
        try {
            var entry: ZipArchiveEntry?
            while (true) {
                try {
                    entry = zipInputStream.nextZipEntry
                } catch (e: MalformedInputException) {
                    Log.d(javaClass, "Filename encoding error", e)
                    // 파일명에서 인코딩 오류 발생 시에, 해당 파일 무시
                    continue
                }

                entry ?: break
                if (entry.isDirectory) {
                    continue
                }

                if (selectedFileName?.equals(entry.name) == false) {
                    continue
                }

                val file = File(destDir, entry.name)
                if (overwrite.not() && file.length() == entry.size) {
                    continue
                }
                val path = file.path
                if (path.lastIndexOf('/') != -1) {
                    val d = File(path.substring(0, path.lastIndexOf('/')))
                    d.mkdirs()
                }

                val out = BufferedOutputStream(FileOutputStream(path))
                val buf = ByteArray(BUFFER_SIZE)
                var readSize: Int
                val prevBytesRead = zipInputStream.bytesRead
                if (encryptor != null) {
                    try {
                        encryptor.encrypt(zipInputStream, out, file.name)
                        continue
                    } catch (e: EncryptionFailedException) {
                        out.close()
                        return false
                    } catch (e: EncryptionUnnecessaryException) {}
                }

                do {
                    readSize = zipInputStream.read(buf)
                    if (readSize <= 0) {
                        break
                    }
                    out.write(buf, 0, readSize)
                    listener?.let {
                        val bytesRead = zipInputStream.bytesRead - prevBytesRead
                        if ((bytesRead / BUFFER_SIZE) % PROGRESS_UPDATE_STEP_SIZE == 0L) {
                            it.onProgress(bytesRead + prevBytesRead)
                        }
                    }
                } while (true)

                out.close()
            }
            return true
        } catch (e: Exception) {
            Log.e(javaClass, "error while unzip", e)
            return false
        } finally {
            zipInputStream.close()
        }
    }

    class EncryptionFailedException(fileName: String) : Exception("error while encrypt $fileName")
    class EncryptionUnnecessaryException(fileName: String) : Exception("$fileName is unnecessary for encrypt")
}
