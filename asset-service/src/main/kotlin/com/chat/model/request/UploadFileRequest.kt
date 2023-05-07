package com.chat.model.request

import org.springframework.web.multipart.MultipartFile
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

data class UploadFileRequest(
    @NotNull(message = "0000") val file: MultipartFile,
    @NotBlank(message = "0000") val s3folder: String
)
