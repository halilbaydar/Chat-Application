package com.chat.model.request

import com.fasterxml.jackson.annotation.JsonProperty
import java.io.Serializable
import javax.validation.constraints.NotBlank

data class DeleteFileRequest(
    @JsonProperty(value = "key", required = true)
    @NotBlank(message = "0000") val key: String,
) : Serializable {
    constructor() : this(key = "")
}
