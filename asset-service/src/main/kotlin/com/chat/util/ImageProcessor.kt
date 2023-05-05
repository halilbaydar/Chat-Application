package com.chat.util

import org.springframework.web.multipart.MultipartFile
import java.awt.image.BufferedImage

interface ImageProcessor {
    fun resizeImage(image: MultipartFile, width: Int, height: Int): BufferedImage

    fun checkImageResolution(image: MultipartFile, resolution: Float): Boolean

    fun convertToX(image: MultipartFile,type:String): MultipartFile
}