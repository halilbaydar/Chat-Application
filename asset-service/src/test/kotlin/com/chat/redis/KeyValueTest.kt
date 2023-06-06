package com.chat.redis

import com.chat.common.BaseTest
import org.junit.jupiter.api.Test
import org.redisson.client.codec.StringCodec
import reactor.test.StepVerifier
import java.util.concurrent.TimeUnit

class KeyValueTest : BaseTest() {

    @Test
    fun keyValueAccessTest() {
        val bucket = redissonReactiveClient?.getBucket<Any>("user:1:name", StringCodec.INSTANCE)
        val set = bucket?.set("sam")
        val get = bucket?.get()?.doOnNext { println(it) }?.then()

        if (set != null && get != null) {
            StepVerifier
                .create(set.concatWith(get))
                .verifyComplete()
        }
    }

    @Test
    fun keyValueAccessExpireTest() {
        val bucket = redissonReactiveClient?.getBucket<Any>("user:1:name", StringCodec.INSTANCE)
        val set = bucket?.set("sam", 10, TimeUnit.SECONDS)
        val get = bucket?.get()?.doOnNext { println(it) }?.then()

        if (set != null && get != null) {
            StepVerifier
                .create(set.concatWith(get))
                .verifyComplete()
        }
    }

    @Test
    fun keyValueAccessExtendExpireTest() {
        val bucket = redissonReactiveClient
            ?.getBucket<Any>("user:1:name", StringCodec.INSTANCE) ?: return

        val set = bucket.set("sam", 10, TimeUnit.SECONDS)
        val get = bucket.get().doOnNext { println(it) }.then()

        StepVerifier
            .create(set.concatWith(get))
            .verifyComplete()

        Thread.sleep(5000)
        val expire = bucket.expire(100, TimeUnit.SECONDS)

        StepVerifier
            .create(expire)
            .expectNext(true)
            .verifyComplete()

        val ttl = bucket
            .remainTimeToLive()
            .doOnNext { println(it) }
            .then()

        StepVerifier
            .create(ttl)
            .verifyComplete()
    }
}