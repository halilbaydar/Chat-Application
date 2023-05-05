package com.chat.util.impl

import com.chat.util.FileProcessor
import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile
import java.awt.image.BufferedImage
import java.io.*
import javax.imageio.IIOImage
import javax.imageio.ImageIO
import javax.imageio.ImageWriteParam.MODE_EXPLICIT
import javax.imageio.stream.ImageOutputStream
import javax.imageio.stream.MemoryCacheImageOutputStream
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi


@Component
class FileProcessorImpl : FileProcessor {
    private val MAGICNUMBER = 0.2f

    @OptIn(ExperimentalEncodingApi::class)
    override fun encodeToString(file: File): String {
        var base64Image = ""
        try {
            FileInputStream(file).use { imageInFile ->
                // Reading a Image file from file system
                val imageData = ByteArray(file.length().toInt())
                imageInFile.read(imageData)
                base64Image = Base64.encode(imageData, 0, imageData.size)
            }
        } catch (e: FileNotFoundException) {
            println("Image not found$e")
        } catch (ioe: IOException) {
            println("Exception while reading the Image $ioe")
        }
        return base64Image
    }

    override fun compress(file: MultipartFile, percentage: Float): MultipartFile {
        val imageName: String = file.originalFilename!!
        val imageExtension = imageName.substring(imageName.lastIndexOf(".") + 1)
        val imageWriter = ImageIO.getImageWritersByFormatName(imageExtension).next()
        val imageWriteParam = imageWriter.defaultWriteParam
        imageWriteParam.compressionMode = MODE_EXPLICIT
        imageWriteParam.compressionQuality = MAGICNUMBER
        val baos = ByteArrayOutputStream()
        val imageOutputStream: ImageOutputStream = MemoryCacheImageOutputStream(baos)
        imageWriter.output = imageOutputStream
        var originalImage: BufferedImage? = null
        try {
            file.inputStream.use { inputStream -> originalImage = ImageIO.read(inputStream) }
        } catch (e: IOException) {
            val info = String.format(
                "compressImage - bufferedImage (file %s)- IOException - message: %s ",
                imageName,
                e.message
            )
            println(info)
            return CustomMultipartFile(baos.toByteArray(), file.name, file.contentType!!)
        }
        val image = IIOImage(originalImage, null, null)
        try {
            imageWriter.write(null, image, imageWriteParam)
        } catch (e: IOException) {
            val info =
                String.format("compressImage - imageWriter (file %s)- IOException - message: %s ", imageName, e.message)
            println(info)
        } finally {
            imageWriter.dispose()
        }
        return CustomMultipartFile(baos.toByteArray(), file.name, file.contentType!!)
    }
}