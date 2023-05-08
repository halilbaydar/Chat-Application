package com.chat.util.impl

import com.amazonaws.services.s3.transfer.model.UploadResult
import com.chat.config.LoggingConfig
import com.chat.exception.constant.FileDownloadFailedException
import com.chat.exception.constant.FileUploadFailedException
import com.chat.model.request.DeleteFileRequest
import com.chat.model.request.UploadFileRequest
import com.chat.property.S3Properties
import com.chat.util.S3FileUploader
import lombok.Getter
import org.springframework.core.io.buffer.DataBuffer
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.http.codec.multipart.FilePart
import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers
import reactor.kotlin.core.publisher.toMono
import software.amazon.awssdk.core.SdkResponse
import software.amazon.awssdk.core.async.AsyncRequestBody
import software.amazon.awssdk.core.async.AsyncResponseTransformer
import software.amazon.awssdk.services.s3.S3AsyncClient
import software.amazon.awssdk.services.s3.model.*
import java.net.URLEncoder
import java.nio.ByteBuffer
import java.nio.charset.StandardCharsets
import java.util.*
import java.util.concurrent.CompletableFuture
import java.util.function.Consumer
import java.util.stream.Collectors

@Component
class S3ProcessorImpl(
    private val s3Properties: S3Properties,
    private val s3Client: S3AsyncClient,
    private val log: LoggingConfig,
) : S3FileUploader {

    private val multipartMinPartSize = 5 * 1024 * 1024

    override fun downloadFile(fileKey: String): Mono<ResponseEntity<Flux<ByteBuffer?>?>?>? {
        val request = GetObjectRequest.builder()
            .bucket(this.s3Properties.bucketName)
            .key(fileKey)
            .build()
        return Mono.fromFuture(this.s3Client.getObject(request, AsyncResponseTransformer.toBytes()))
            .map { response ->
                checkResult(response.response())
                val filename = getMetadataItem(response.response(), "filename", fileKey)
                log.s3.info(
                    "[I65] filename={}, length={}", filename, response.response()
                        .contentLength()
                )
                ResponseEntity.ok().header(
                    HttpHeaders.CONTENT_TYPE, response.response()
                        .contentType()
                )
                    .header(
                        HttpHeaders.CONTENT_LENGTH, response.response()
                            .contentLength().toString()
                    )
                    .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"$filename\""
                    )
                    .body(Flux.from { response })
            }
    }

    private fun getMetadataItem(sdkResponse: GetObjectResponse, filename: String, defaultValue: String): String? {
        for ((key1, value) in sdkResponse.metadata()) {
            if (key1.equals(filename, ignoreCase = true)) {
                return value
            }
        }
        return defaultValue
    }

    // Helper used to check return codes from an API call
    private fun checkResult(response: GetObjectResponse) {
        val sdkResponse = response.sdkHttpResponse()
        if (sdkResponse != null && sdkResponse.isSuccessful) {
            return
        }
        throw FileDownloadFailedException(response.toString())
    }

    override fun uploadFile(
        headers: HttpHeaders,
        filUploadFileRequest: Mono<UploadFileRequest>
    ): Mono<ResponseEntity<UploadResult>> {
        return filUploadFileRequest.flatMap { fileRequest ->
            if (headers.contentLength < 0) {
                Mono.error(FileUploadFailedException("required header missing: Content-Length"))
            } else {
                val fileKey = generateFileUrl(fileRequest.fileName, fileRequest.s3folder)
                var mediaType = headers.contentType

                if (mediaType == null) {
                    mediaType = MediaType.APPLICATION_OCTET_STREAM
                }

                val metadata = HashMap<String, String>()

                this.s3Client.putObject(
                    PutObjectRequest.builder()
                        .bucket(this.s3Properties.bucketName)
                        .contentLength(headers.contentLength)
                        .key(fileKey)
                        .metadata(metadata)
                        .contentType(mediaType.toString())
                        .build(),
                    AsyncRequestBody.fromPublisher(fileRequest.parts)
                ).toMono()
                    .map {
                        val result = UploadResult()
                        result.key = fileKey
                        result.bucketName = this.s3Properties.bucketName
                        return@map ResponseEntity.status(HttpStatus.CREATED).body(result)
                    }
            }
        }
    }


    protected fun saveFile(headers: HttpHeaders?, part: FilePart): Mono<String> {

        // Generate a filekey for this upload
        val fileKey = UUID.randomUUID().toString()
        log.s3.info("[I137] saveFile: filekey={}, filename={}", fileKey, part.filename())

        // Gather metadata
        val metadata: MutableMap<String, String?> = HashMap()
        val filename = part.filename()
        metadata["filename"] = filename
        var mt = part.headers().contentType
        if (mt == null) {
            mt = MediaType.APPLICATION_OCTET_STREAM
        }

        // Create multipart upload request
        val uploadRequest: CompletableFuture<CreateMultipartUploadResponse> = this.s3Client
            .createMultipartUpload(
                CreateMultipartUploadRequest.builder()
                    .contentType(mt.toString())
                    .key(fileKey)
                    .metadata(metadata)
                    .bucket(this.s3Properties.bucketName)
                    .build()
            )

        // This variable will hold the upload state that we must keep
        // around until all uploads complete
        val uploadState = UploadState(this.s3Properties.bucketName, fileKey)
        return Mono
            .fromFuture(uploadRequest)
            .flatMapMany { response: CreateMultipartUploadResponse ->
                checkResult(response)
                uploadState.uploadId = response.uploadId()
                log.s3.info("[I183] uploadId={}", response.uploadId())
                part.content()
            }
            .bufferUntil { buffer: DataBuffer ->
                uploadState.buffered += buffer.readableByteCount()
                if (uploadState.buffered >= multipartMinPartSize) {
                    log.s3.info(
                        "[I173] bufferUntil: returning true, bufferedBytes={}, partCounter={}, uploadId={}",
                        uploadState.buffered,
                        uploadState.partCounter,
                        uploadState.uploadId
                    )
                    uploadState.buffered = 0
                    return@bufferUntil true
                } else {
                    return@bufferUntil false
                }
            }
            .map { buffers: List<DataBuffer>? ->
                concatBuffers(
                    buffers!!
                )
            }
            .flatMap { buffer: ByteBuffer? ->
                uploadPart(
                    uploadState,
                    buffer!!
                )
            }
            .onBackpressureBuffer()
            .reduce(uploadState) { state: UploadState, completedPart ->
                log.s3.info(
                    "[I188] completed: partNumber={}, etag={}",
                    completedPart.partNumber(),
                    completedPart.eTag()
                )
                state.completedParts[completedPart.partNumber()] = completedPart
                state
            }
            .flatMap { state: UploadState? -> completeUpload(state!!) }
            .map { response: SdkResponse? ->
                checkResult(response!!)
                uploadState.fileKey
            }
    }

    private fun completeUpload(state: UploadState): Mono<CompleteMultipartUploadResponse?> {
        log.s3.info(
            "[I202] completeUpload: bucket={}, filekey={}, completedParts.size={}",
            state.bucket,
            state.fileKey,
            state.completedParts.size
        )
        val multipartUpload: CompletedMultipartUpload = CompletedMultipartUpload.builder()
            .parts(state.completedParts.values)
            .build()
        return Mono.fromFuture(
            this.s3Client.completeMultipartUpload(
                CompleteMultipartUploadRequest.builder()
                    .bucket(state.bucket)
                    .uploadId(state.uploadId)
                    .multipartUpload(multipartUpload)
                    .key(state.fileKey)
                    .build()
            )
        )
    }

    private fun concatBuffers(buffers: List<DataBuffer>): ByteBuffer {
        log.s3.info("[I198] creating BytBuffer from {} chunks", buffers.size)
        var partSize = 0
        for (b in buffers) {
            partSize += b.readableByteCount()
        }
        val partData: ByteBuffer = ByteBuffer.allocate(partSize)
        buffers.forEach(Consumer { buffer: DataBuffer ->
            partData.put(
                buffer.asByteBuffer()
            )
        })

        // Reset read pointer to first byte
        partData.rewind()
        log.s3.info("[I208] partData: size={}", partData.capacity())
        return partData
    }

    private fun uploadPart(uploadState: UploadState, buffer: ByteBuffer): Mono<CompletedPart> {
        val partNumber: Int = ++uploadState.partCounter
        log.s3.info("[I218] uploadPart: partNumber={}, contentLength={}", partNumber, buffer.capacity())
        val request: CompletableFuture<UploadPartResponse> = this.s3Client.uploadPart(
            UploadPartRequest.builder()
                .bucket(uploadState.bucket)
                .key(uploadState.fileKey)
                .partNumber(partNumber)
                .uploadId(uploadState.uploadId)
                .contentLength(buffer.capacity().toLong())
                .build(),
            AsyncRequestBody.fromPublisher(Mono.just(buffer))
        )
        return Mono
            .fromFuture(request)
            .map { uploadPartResult: UploadPartResponse ->
                checkResult(uploadPartResult)
                log.s3.info("[I230] uploadPart complete: part={}, etag={}", partNumber, uploadPartResult.eTag())
                CompletedPart.builder()
                    .eTag(uploadPartResult.eTag())
                    .partNumber(partNumber)
                    .build()
            }
    }


    @Getter
    internal class UploadState(val bucket: String, val fileKey: String) {
        var uploadId: String? = null
        var partCounter = 0
        var completedParts = HashMap<Int, CompletedPart>()
        var buffered = 0
    }


    private fun checkResult(result: SdkResponse) {
        if (result.sdkHttpResponse() == null || !result.sdkHttpResponse().isSuccessful) {
            throw FileUploadFailedException(result.toString())
        }
    }

    override fun uploadFileByParts(
        headers: HttpHeaders,
        fileUploadFileRequest: Mono<UploadFileRequest>
    ): Mono<ResponseEntity<List<UploadResult>>> {
        return fileUploadFileRequest.flatMap { uploadFileRequest ->
            uploadFileRequest.parts
                .ofType(FilePart::class.java)
                .flatMap { part -> saveFile(headers, part) }
                .collect(Collectors.toList())
                .map {
                    val keys = it.stream().map {
                        val result = UploadResult()
                        result.key = it
                        result.bucketName = this.s3Properties.bucketName
                        return@map result
                    }.collect(Collectors.toList())
                    ResponseEntity.status(HttpStatus.CREATED).body(keys)
                }
        }
    }

    override fun copyFileInS3Bucket(sourceKey: String, destinationKey: String): Boolean {
        TODO("Not yet implemented")
    }

    override fun deleteFile(deleteFileRequest: Mono<DeleteFileRequest>): Mono<DeleteObjectResponse> {
        return deleteFileRequest.flatMap {
            val key = this.getFileKeyFromUrl(it.key)
            return@flatMap this.s3Client.deleteObject(
                DeleteObjectRequest.builder()
                    .bucket(this.s3Properties.bucketName)
                    .key(key)
                    .build()
            ).toMono()
        }
    }

    override fun softDeleteFile(deleteFileRequest: Mono<DeleteFileRequest>): Mono<CopyObjectResponse> {
        return deleteFileRequest.map { deleteFile ->
            val decodedSource = URLEncoder.encode(
                "${this.s3Properties.bucketName}/${deleteFile.key}",
                StandardCharsets.UTF_8.toString()
            )
            CopyObjectRequest.builder()
                .copySource(decodedSource)
                .bucket(deleteFile.destinationBucket)
                .key(deleteFile.destinationKey)
                .build()
        }
            .flatMap { copyRequest ->
                this.s3Client.copyObject(
                    copyRequest
                ).toMono()
            }
            .subscribeOn(Schedulers.boundedElastic())
    }

    override fun uploadFiles(files: Array<MultipartFile>, folder: String): Boolean {
        TODO("Not yet implemented")
    }

    private fun generateFileUrl(filename: String, folder: String): String {
        return "$folder/$filename"
    }

    private fun getFileKeyFromUrl(url: String): String {
        return url.substring(url.lastIndexOf(".com/") + 5)
    }
}