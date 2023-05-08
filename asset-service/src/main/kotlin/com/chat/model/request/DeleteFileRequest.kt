package com.chat.model.request

import javax.validation.constraints.NotBlank

data class DeleteFileRequest(
    @NotBlank(message = "0000") val key: String,
    @NotBlank(message = "0000") val destinationBucket: String,
    @NotBlank(message = "0000") val destinationKey: String,
)
