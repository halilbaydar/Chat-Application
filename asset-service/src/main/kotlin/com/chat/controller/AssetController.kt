package com.chat.controller

import com.amazonaws.services.s3.transfer.model.UploadResult
import com.chat.model.request.DeleteFileRequest
import com.chat.model.request.UploadFileRequest
import com.chat.service.AssetService
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import software.amazon.awssdk.services.s3.model.CopyObjectResponse
import software.amazon.awssdk.services.s3.model.DeleteObjectResponse
import java.nio.ByteBuffer
import javax.validation.constraints.NotBlank


@RestController
@RequestMapping(path = ["/v1/asset/upload"])
class AssetController(private val assetService: AssetService) {

    @PostMapping(consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun assetUpload(
        @RequestHeader headers: HttpHeaders,
        @RequestBody uploadRequest: Mono<UploadFileRequest>
    ): Mono<ResponseEntity<UploadResult>> {
        return this.assetService.assetUpload(headers, uploadRequest)
    }

    @PostMapping(path = ["parts"], consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun assetUploadByParts(
        @RequestHeader headers: HttpHeaders,
        @RequestBody uploadRequest: Mono<UploadFileRequest>
    ): Mono<ResponseEntity<List<UploadResult>>> {
        return this.assetService.assetUploadByParts(headers, uploadRequest)
    }

    @DeleteMapping
    fun assetDelete(@ModelAttribute deleteFileRequest: Mono<DeleteFileRequest>): Mono<DeleteObjectResponse> {
        return this.assetService.assetDelete(deleteFileRequest)
    }

    @DeleteMapping("/soft")
    fun assetSoftDelete(@ModelAttribute deleteFileRequest: Mono<DeleteFileRequest>): Mono<CopyObjectResponse> {
        return this.assetService.assetSoftDelete(deleteFileRequest)
    }

    @GetMapping(path = ["/{fileKey}"])
    fun downloadFile(@PathVariable("fileKey") @NotBlank fileKey: String): Mono<ResponseEntity<Flux<ByteBuffer?>?>?>? {
        return this.assetService.downloadFile(fileKey)
    }
}