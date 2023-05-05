package com.chat.util.impl

import com.amazonaws.AmazonClientException
import com.amazonaws.AmazonServiceException
import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.model.GetObjectRequest
import com.amazonaws.services.s3.model.S3Object
import com.amazonaws.services.s3.model.S3ObjectInputStream
import com.chat.property.S3Properties
import com.chat.util.S3FileUploader
import lombok.extern.slf4j.Slf4j
import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile
import java.io.IOException

@Slf4j
@Component
class S3ProcessorImpl(
    private val imageProcessor: ImageProcessorImpl,
    private val s3Properties: S3Properties,
    private val s3Client: AmazonS3Client
) :
    S3FileUploader {
    override fun downloadFileAsX(keyName: String, type: String): MultipartFile? {
        try {
            val s3object: S3Object = s3Client.getObject(GetObjectRequest(s3Properties.bucketName, keyName))
            val outputStream: S3ObjectInputStream = s3object.objectContent
            return imageProcessor.convertToX(
                CustomMultipartFile(
                    outputStream.readAllBytes(),
                    s3object.key,
                    s3object.objectMetadata.contentType
                ), type
            )
        } catch (ase: AmazonServiceException) {
        } catch (ace: AmazonClientException) {
        } catch (ioe: IOException) {
        }

        return null
    }

    override fun uploadFile(file: MultipartFile, folder: String): Boolean {
        TODO("Not yet implemented")
    }

    override fun copyFileInS3Bucket(sourceKey: String, destinationKey: String): Boolean {
        TODO("Not yet implemented")
    }

    override fun deleteFile(url: String): Boolean {
        TODO("Not yet implemented")
    }

    override fun softDeleteFile(url: String): Boolean {
        TODO("Not yet implemented")
    }

    override fun uploadFiles(files: Array<MultipartFile>, folder: String): Boolean {
        TODO("Not yet implemented")
    }
}