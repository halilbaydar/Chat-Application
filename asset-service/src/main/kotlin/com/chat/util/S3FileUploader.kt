package com.chat.util

import com.amazonaws.services.s3.transfer.model.UploadResult
import com.chat.model.request.CopyFileRequest
import com.chat.model.request.DeleteFileRequest
import com.chat.model.request.UploadFileRequest
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.web.multipart.MultipartFile
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import software.amazon.awssdk.services.s3.model.CopyObjectResponse
import software.amazon.awssdk.services.s3.model.DeleteObjectResponse
import java.nio.ByteBuffer

interface S3FileUploader {
    fun downloadFile(fileKey: String): Mono<ResponseEntity<Flux<ByteBuffer?>?>?>?

    fun uploadFile(
        headers: HttpHeaders,
        filUploadFileRequest: Mono<UploadFileRequest>
    ): Mono<ResponseEntity<UploadResult>>

    fun copyFileInS3Bucket(copyFileRequest: Mono<CopyFileRequest>): Mono<CopyObjectResponse>

    fun uploadFileByParts(
        headers: HttpHeaders,
        fileUploadFileRequest: Mono<UploadFileRequest>
    ): Mono<ResponseEntity<List<UploadResult>>>

    fun deleteFile(deleteFileRequest: Mono<DeleteFileRequest>): Mono<DeleteObjectResponse>

    fun softDeleteFile(copyFileRequest: Mono<CopyFileRequest>): Mono<CopyObjectResponse>

    fun uploadFiles(files: Array<MultipartFile>, folder: String): Boolean
}