package com.chat.util.impl

import lombok.Getter
import lombok.Setter
import org.springframework.web.multipart.MultipartFile
import java.io.*

@Getter
@Setter
class CustomMultipartFile(
    private val imgContent: ByteArray,
    private val name: String,
    private val contentType: String
) : MultipartFile, Serializable {

    override fun getInputStream(): InputStream {
        return ByteArrayInputStream(imgContent)
    }

    override fun getName(): String {
        return name
    }

    override fun getOriginalFilename(): String {
        return name
    }

    override fun getContentType(): String {
        return contentType
    }

    override fun isEmpty(): Boolean {
        return imgContent.isEmpty()
    }

    override fun getSize(): Long {
        return imgContent.size.toLong()
    }

    override fun getBytes(): ByteArray {
        return imgContent
    }

    override fun transferTo(dest: File) {
        FileOutputStream(dest).write(imgContent)
    }

}
