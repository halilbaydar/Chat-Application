package com.chat.service

import com.chat.model.request.DeleteFileRequest
import com.chat.model.request.UploadFileRequest
import com.chat.util.impl.S3ProcessorImpl
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers
import reactor.util.retry.Retry
import java.time.Duration

@Service
class UploadService(private val s3ProcessorImpl: S3ProcessorImpl) {
    private val BASE_URL = "aws::dummy-url"

    fun assetUpload(fileRequest: Mono<UploadFileRequest>): Mono<String> {
        return fileRequest.map {
            this.s3ProcessorImpl.uploadFile(it.file, it.s3folder)
        }
            .retryWhen(Retry.fixedDelay(5, Duration.ofSeconds(2)))
            .map {
                if (it) {
                    BASE_URL
                } else {
                    ""
                }
            }.subscribeOn(Schedulers.boundedElastic())
    }

    fun assetDelete(deleteFileRequest: Mono<DeleteFileRequest>): Mono<Boolean> {
        return deleteFileRequest.map {
            this.s3ProcessorImpl.deleteFile(it.url)
        }.subscribeOn(Schedulers.boundedElastic())
    }

    fun assetSoftDelete(deleteFileRequest: Mono<DeleteFileRequest>): Mono<Boolean> {
        return deleteFileRequest.map {
            this.s3ProcessorImpl.softDeleteFile(it.url)
        }.subscribeOn(Schedulers.boundedElastic())
    }
}