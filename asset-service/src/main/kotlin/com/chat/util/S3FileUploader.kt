package com.chat.util

import org.springframework.web.multipart.MultipartFile

interface S3FileUploader {
    fun downloadFileAsJPG(keyName: String): MultipartFile

    fun uploadFile(file: MultipartFile, folder: String): Boolean

    fun copyFileInS3Bucket(sourceKey: String, destinationKey: String): Boolean

    fun deleteFile(url: String): Boolean

    fun softDeleteFile(url: String): Boolean

    fun uploadFiles(files: Array<MultipartFile>, folder: String): Boolean
}