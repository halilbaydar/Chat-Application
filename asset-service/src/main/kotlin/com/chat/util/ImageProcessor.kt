package com.chat.util

import org.springframework.web.multipart.MultipartFile

interface ImageProcessor {
    fun resizeImage(image: MultipartFile, resolution: Float): MultipartFile

    fun checkImageResolution(image: MultipartFile, resolution: Float): Boolean

    fun convertToJpeg(image: MultipartFile): MultipartFile
}