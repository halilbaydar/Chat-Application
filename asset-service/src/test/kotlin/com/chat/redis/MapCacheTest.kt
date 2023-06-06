package com.chat.redis

import com.chat.common.BaseTest
import com.chat.model.request.DeleteFileRequest
import org.junit.jupiter.api.Test
import org.redisson.codec.TypedJsonJacksonCodec
import reactor.test.StepVerifier
import java.util.concurrent.TimeUnit

class MapCacheTest : BaseTest() {

    @Test
    fun mapCacheTest() {
        val codec = TypedJsonJacksonCodec(String::class.java, DeleteFileRequest::class.java)
        val bucket = this.redissonReactiveClient
            ?.getMapCache<String, DeleteFileRequest>("file:s3:delete:cache", codec) ?: return

        val mono1 = bucket.put("file:1", DeleteFileRequest("key1"), 100, TimeUnit.SECONDS)
        val mono2 = bucket.put("file:2", DeleteFileRequest("key2"), 50, TimeUnit.SECONDS)
        val mono3 = bucket.put("file:3", DeleteFileRequest("key3"), 10, TimeUnit.SECONDS)
        val mono4 = bucket.put("file:4", DeleteFileRequest("key4"), 3, TimeUnit.SECONDS)

        assert(true)

        StepVerifier
            .create(
                mono1.then(mono2)
                    .then(mono3)
                    .then(mono4)
            ).verifyComplete()

        bucket.get("file:1")
            .doOnNext { println(it) }
            .subscribe()
    }
}