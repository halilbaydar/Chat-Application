package com.chat.kafka.sec15

data class TransferEvent(
    val key: String,
    val from: String,
    val to: String,
    val amount: String,
    val ack: Runnable
)
