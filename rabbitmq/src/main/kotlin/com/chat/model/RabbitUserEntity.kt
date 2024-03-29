package com.chat.model

import java.io.Serializable
import java.math.BigInteger


data class RabbitUserEntity(
    val id: BigInteger,
    val username: String,
    val name: String,
    val roles: String,
    val status: String,
    val password: String,
    val createdAt: Long,
    val updatedAt: Long
) : Serializable {
    constructor() : this(BigInteger.valueOf(1L), "", "", "", "", "", 1L, 1L)
}
