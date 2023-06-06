package com.chat.redis.model

import java.io.Serializable

data class Customer(val id: Long, val type: CustomerType, val name: String) : Serializable {
    constructor() : this(0, CustomerType.GUEST, "")
}
