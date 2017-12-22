package com.ridi.books.helper.io

import com.ridi.books.helper.Log
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream
import java.io.*
import java.nio.charset.MalformedInputException

object ZipHelper {
    private val bufferSize = 8192
    private val progressUpdateStepSize = bufferSize / 16

    enum class EncryptResult {
        SUCCESS, FAIL, UNNECESSARY
    }

    interface Listener {
        fun onProgress(unzippedBytes: Long)
    }

    interface Encryptor {
        fun onEncrypt(zipArchiveInputStream: ZipArchiveInputStream, outputStream: OutputStream, fileName: String): EncryptResult
    }

    @JvmStatic
    @JvmOverloads
    fun unzip(zipFile: File, destDir: File, deleteZip: Boolean, listener: Listener? = null,
              encodings: List<String> = listOf("UTF8"), overwrite: Boolean = true): Boolean {
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
    fun unzipEncryptIfNeeded(inputStream: InputStream, destDir: File, listener: Listener?, encryptor: Encryptor?): Boolean {
        return unzip(inputStream, destDir, listener, encryptor)
    }

    @JvmStatic
    fun unzipSelectedFile(inputStream: InputStream, destDir: File, selectedFileName: String): Boolean {
        return unzip(inputStream, destDir, selectedFileName = selectedFileName)
    }

    @JvmStatic
    @JvmOverloads
    fun unzip(inputStream: InputStream, destDir: File, listener: Listener? = null, encryptor: Encryptor? = null,
              encoding: String = "UTF8", overwrite: Boolean = true, selectedFileName: String? = null): Boolean {
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
                val buf = ByteArray(bufferSize)
                var readSize: Int
                val prevBytesRead = zipInputStream.bytesRead
                if (encryptor != null) {
                    val result = encryptor.onEncrypt(zipInputStream, out, file.name)
                    if (isEncryptSuccess(result)) {
                        if (result == EncryptResult.SUCCESS) {
                            continue
                        }
                    } else {
                        out.close()
                        return false
                    }
                }

                do {
                    readSize = zipInputStream.read(buf)
                    if (readSize <= 0) {
                        break
                    }
                    out.write(buf, 0, readSize)
                    listener?.let {
                        val bytesRead = zipInputStream.bytesRead - prevBytesRead
                        if ((bytesRead / bufferSize) % progressUpdateStepSize == 0L) {
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

    private fun isEncryptSuccess(encryptResult: EncryptResult): Boolean = (encryptResult == EncryptResult.FAIL).not()
}
