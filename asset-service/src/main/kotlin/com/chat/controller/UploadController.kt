package com.chat.controller

import com.chat.model.request.DeleteFileRequest
import com.chat.model.request.UploadFileRequest
import com.chat.service.UploadService
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono

@RestController
@RequestMapping(path = ["/v1/asset/upload"])
class UploadController(private val uploadService: UploadService) {

    @PostMapping
    fun assetUpload(@ModelAttribute fileRequest: Mono<UploadFileRequest>): Mono<String> {
        return this.uploadService.assetUpload(fileRequest)
    }

    @DeleteMapping
    fun assetDelete(@ModelAttribute deleteFileRequest: Mono<DeleteFileRequest>): Mono<Boolean> {
        return this.uploadService.assetDelete(deleteFileRequest)
    }

    @DeleteMapping("soft")
    fun assetSoftDelete(@ModelAttribute deleteFileRequest: Mono<DeleteFileRequest>): Mono<Boolean> {
        return this.uploadService.assetSoftDelete(deleteFileRequest)
    }
}