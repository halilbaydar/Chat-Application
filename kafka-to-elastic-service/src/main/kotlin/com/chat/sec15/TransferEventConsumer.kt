package com.chat.kafka.sec15

import reactor.core.publisher.Flux
import reactor.kafka.receiver.KafkaReceiver
import reactor.kafka.receiver.ReceiverRecord

class TransferEventConsumer(private val kafkaReceiver: KafkaReceiver<String, String>) {


    fun receive(): Flux<TransferEvent> {
        return this.kafkaReceiver.receive().map { transferTo(it) }
    }

    private fun transferTo(record: ReceiverRecord<String, String>): TransferEvent {
        val arr = record.value().split(",")
        val ack = if (record.key().endsWith("6")) {
            {
                record.receiverOffset().acknowledge()
            }
        } else {
            {
                throw RuntimeException()
            }
        }
        return TransferEvent(
            record.key(),
            arr[0],
            arr[1],
            arr[2],
            ack
        )
    }
}