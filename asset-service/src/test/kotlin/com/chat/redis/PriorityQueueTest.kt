package com.chat.redis

import com.chat.common.BaseTest
import com.chat.redis.model.Customer
import com.chat.redis.model.CustomerType
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.redisson.api.RBlockingDequeReactive
import org.redisson.codec.TypedJsonJacksonCodec
import reactor.core.publisher.Flux
import reactor.test.StepVerifier
import java.time.Duration

class PriorityQueueTest : BaseTest() {

    private lateinit var messageQueue: RBlockingDequeReactive<Customer>

    @BeforeAll
    fun setupQueue() {
        this.messageQueue =
            this.redissonReactiveClient?.getBlockingDeque(
                "test:1",
                TypedJsonJacksonCodec(Customer::class.java)
            )
                ?: return
    }

    @Test
    fun consumer1() {
        this.messageQueue
            .takeElements()
            .doOnNext { println("Consumer 1. Name: ${it.name}, type: ${it.type}") }
            .doOnError(System.out::println)
            .subscribe()

        Thread.sleep(1000000)
    }

    @Test
    fun consumer2() {
        this.messageQueue
            .takeElements()
            .doOnNext { println("Consumer 2. Name: ${it.name}, type: ${it.type}") }
            .doOnError(System.out::println)
            .subscribe()

        Thread.sleep(1000000)
    }

    @Test
    fun producer1() {
        StepVerifier.create(
            Flux.range(0, 100)
                .delayElements(Duration.ofSeconds(1))
                .doOnNext { println("Message sent: $it") }
                .flatMap {
                    var type: CustomerType
                    if (it % 3 == 0) {
                        type = CustomerType.PRIME
                    } else if (it % 3 == 1) {
                        type = CustomerType.STD
                    } else {
                        type = CustomerType.GUEST
                    }
                    this.messageQueue.add(Customer(it.toLong(), type, "$it"))
                }
                .then()
        ).verifyComplete()
    }
}