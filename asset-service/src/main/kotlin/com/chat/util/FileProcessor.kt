package com.chat.util

import org.springframework.web.multipart.MultipartFile
import java.io.File

interface FileProcessor {
    fun encodeToString(file: File): String

    fun compress(file: MultipartFile, percentage: Float): MultipartFile
}