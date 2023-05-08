package com.chat.model.request

import reactor.core.publisher.Flux
import java.nio.ByteBuffer
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

data class UploadFileRequest(
    @NotNull(message = "0000") val parts: Flux<ByteBuffer>,
    @NotBlank(message = "0000") val s3folder: String,
    @NotBlank(message = "0000") val fileName: String
)
