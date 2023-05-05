package com.chat.util.impl

import com.chat.exception.CustomException
import com.chat.exception.constant.ExceptionConstants
import com.chat.util.ImageProcessor
import org.springframework.web.multipart.MultipartFile
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import java.io.IOException
import javax.imageio.ImageIO


open class ImageProcessorImpl : FileProcessorImpl(), ImageProcessor {
    private val IMAGE_MAX_RESOLUTION_RATIO = 1.0

    override fun resizeImage(image: MultipartFile, width: Int, height: Int): BufferedImage {
        val inputImage = ImageIO.read(image.inputStream)
        val bufferedImage = BufferedImage(width, height, inputImage.type)
        val graphics2D = bufferedImage.createGraphics()
        graphics2D.drawImage(inputImage, 0, 0, width, height, null)
        graphics2D.dispose()
        return bufferedImage
    }

    override fun checkImageResolution(image: MultipartFile, resolution: Float): Boolean {
        val bufferedImage: BufferedImage = ImageIO.read(image.inputStream)
        val width = bufferedImage.width
        val height = bufferedImage.height

        val ratio = width.toDouble() / height.toDouble()
        val maxRatioLimit: Double = IMAGE_MAX_RESOLUTION_RATIO
        if (ratio < 1 / maxRatioLimit || ratio > maxRatioLimit) {
            throw CustomException(ExceptionConstants.IMAGE_WIDTH_TO_HEIGHT_RATIO_UNACCEPTABLE)
        }

        return width > resolution || height > resolution
    }

    override fun convertToX(image: MultipartFile, type: String): MultipartFile {
        var bufferedImage: BufferedImage? = null

        try {
            bufferedImage = ImageIO.read(image.inputStream)
        } catch (e: IOException) {
            e.printStackTrace()
        }

        val baos = ByteArrayOutputStream()

        try {
            assert(bufferedImage != null)
            ImageIO.write(bufferedImage, type, baos)
            baos.flush()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return CustomMultipartFile(baos.toByteArray(), image.name, image.contentType!!)
    }
}