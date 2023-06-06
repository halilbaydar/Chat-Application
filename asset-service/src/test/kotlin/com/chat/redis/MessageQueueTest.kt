package com.chat.redis

import com.chat.common.BaseTest
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.redisson.api.RBlockingDequeReactive
import org.redisson.client.codec.LongCodec
import reactor.core.publisher.Flux
import reactor.test.StepVerifier
import java.time.Duration

class MessageQueueTest : BaseTest() {
    private lateinit var messageQueue: RBlockingDequeReactive<Long>

    @BeforeAll
    fun setupQueue() {
        this.messageQueue = this.redissonReactiveClient?.getBlockingDeque("test:1", LongCodec.INSTANCE) ?: return
    }

    @Test
    fun consumer1() {
        this.messageQueue
            .takeElements()
            .doOnNext { println("Consumer 1: $it") }
            .doOnError(System.out::println)
            .subscribe()

        Thread.sleep(1000000)
    }

    @Test
    fun consumer2() {
        this.messageQueue
            .takeElements()
            .doOnNext { println("Consumer 2: $it") }
            .doOnError(System.out::println)
            .subscribe()

        Thread.sleep(1000000)
    }

    @Test
    fun producer1() {
        StepVerifier.create(Flux.range(0, 100)
            .delayElements(Duration.ofSeconds(1))
            .doOnNext { println("Message sent: $it") }
            .flatMap { this.messageQueue.add(it?.toLong()) }
            .then()
        ).verifyComplete()
    }
}