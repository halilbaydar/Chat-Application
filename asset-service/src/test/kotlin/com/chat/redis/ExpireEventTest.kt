package com.chat.redis

import com.chat.common.BaseTest
import org.junit.jupiter.api.Test
import org.redisson.api.DeletedObjectListener
import org.redisson.api.ExpiredObjectListener
import org.redisson.client.codec.StringCodec
import reactor.test.StepVerifier
import java.util.concurrent.TimeUnit

class ExpireEventTest : BaseTest() {

    @Test
    fun expireEventTest() {
        val bucket = redissonReactiveClient?.getBucket<Any>("user:1:name", StringCodec.INSTANCE) ?: return
        val set = bucket.set("sam", 10, TimeUnit.SECONDS)
        val get = bucket.get().doOnNext { println(it) }.then()

        val expiredListener = bucket.addListener(ExpiredObjectListener { key ->
            println("Expired event : $key")
        }).then()

        StepVerifier
            .create(set.concatWith(get).concatWith(expiredListener))
            .verifyComplete()

        Thread.sleep(11000)
    }

    @Test
    fun deletedEventTest() {
        val bucket = redissonReactiveClient?.getBucket<Any>("user:1:name", StringCodec.INSTANCE) ?: return
        val set = bucket.set("sam", 10, TimeUnit.SECONDS)
        val get = bucket.get().doOnNext { println(it) }.then()

        val deletedListener = bucket.addListener(DeletedObjectListener { key ->
            println("Deleted event : $key")
        }).then()

        StepVerifier
            .create(
                set.concatWith(get)
                    .concatWith(deletedListener)
            )
            .verifyComplete()

        Thread.sleep(60000)
    }
}