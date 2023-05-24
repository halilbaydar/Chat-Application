package com.chat

import com.chat.kafka.avro.model.UserAvroModel
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import reactor.test.StepVerifier
import java.time.Duration

class UserElasticEventProducerTest : AbstractIT() {

    @Test
    fun producerTest() {
        val receiver = createReceiver<UserAvroModel>(listOf("user-to-elastic"))
        val events = receiver
            .receive()
            .take(1)
            .doOnNext { println("Message received key: ${it.key()}, value: ${it.value()}") }
            .doOnNext { it.receiverOffset().acknowledge() }

        StepVerifier.create(events)
            .consumeNextWith { event ->
                Assertions.assertNotNull(event.value().username)
            }
            .expectNextCount(1)
            .expectComplete()
            .verify(Duration.ofSeconds(10))
    }
}