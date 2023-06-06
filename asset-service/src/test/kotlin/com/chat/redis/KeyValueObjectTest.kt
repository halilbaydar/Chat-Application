package com.chat.redis

import com.chat.common.BaseTest
import com.chat.model.request.DeleteFileRequest
import org.junit.jupiter.api.Test
import org.redisson.codec.TypedJsonJacksonCodec
import reactor.test.StepVerifier

class KeyValueObjectTest : BaseTest() {

    @Test
    fun keyValueObjectAccessTest() {
        val bucket = redissonReactiveClient?.getBucket<Any>(
            "file:s3:process:delete",
            TypedJsonJacksonCodec(DeleteFileRequest::class.java)
        )
        val set = bucket?.set(DeleteFileRequest("key"))
        val get = bucket?.get()?.doOnNext { println(it) }?.then()

        if (set != null && get != null) {
            StepVerifier
                .create(set.concatWith(get))
                .verifyComplete()
        }
    }

    @Test
    fun keyValueAccessExpireTest() {

    }

    @Test
    fun keyValueAccessExtendExpireTest() {

    }
}