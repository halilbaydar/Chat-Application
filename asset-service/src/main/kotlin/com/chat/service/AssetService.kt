package com.chat.service

import com.amazonaws.services.s3.transfer.model.UploadResult
import com.chat.model.request.DeleteFileRequest
import com.chat.model.request.UploadFileRequest
import com.chat.util.impl.S3ProcessorImpl
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import software.amazon.awssdk.services.s3.model.CopyObjectResponse
import software.amazon.awssdk.services.s3.model.DeleteObjectResponse
import java.nio.ByteBuffer

@Service
class AssetService(private val s3ProcessorImpl: S3ProcessorImpl) {

    fun assetUpload(headers: HttpHeaders, fileRequest: Mono<UploadFileRequest>): Mono<ResponseEntity<UploadResult>> {
        return this.s3ProcessorImpl.uploadFile(headers, fileRequest)
    }

    fun assetDelete(deleteFileRequest: Mono<DeleteFileRequest>): Mono<DeleteObjectResponse> {
        return this.s3ProcessorImpl.deleteFile(deleteFileRequest)
    }

    fun assetSoftDelete(deleteFileRequest: Mono<DeleteFileRequest>): Mono<CopyObjectResponse> {
        return this.s3ProcessorImpl.softDeleteFile(deleteFileRequest)
    }

    fun downloadFile(fileKey: String): Mono<ResponseEntity<Flux<ByteBuffer?>?>?>? {
        return this.s3ProcessorImpl.downloadFile(fileKey)
    }
}