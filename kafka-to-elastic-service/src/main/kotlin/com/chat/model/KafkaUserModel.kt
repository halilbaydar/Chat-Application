package com.chat.model

data class KafkaUserModel(
    internal val id: Long,
    internal val name: String,
    internal val username: String,
    internal val role: String,
    internal val createdAt: Long,
    internal val updatedAt: Long
)
