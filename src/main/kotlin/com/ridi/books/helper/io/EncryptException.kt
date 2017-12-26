package com.ridi.books.helper.io

open class EncryptException(message: String) : Exception(message)
class EncryptFailException(fileName: String) : EncryptException("error while encrypt $fileName")
class EncryptUnnecessaryException(fileName: String) : EncryptException("$fileName is unnecessary for encrypt")