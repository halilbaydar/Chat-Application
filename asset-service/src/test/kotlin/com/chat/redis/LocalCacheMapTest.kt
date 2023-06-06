package com.chat.redis

import com.chat.common.BaseTest
import com.chat.model.request.DeleteFileRequest
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.redisson.api.LocalCachedMapOptions
import org.redisson.api.RLocalCachedMap
import org.redisson.codec.TypedJsonJacksonCodec
import reactor.core.publisher.Flux
import java.time.Duration

class LocalCacheMapTest : BaseTest() {

    private lateinit var cacheMap: RLocalCachedMap<String, DeleteFileRequest>

    @BeforeAll
    fun setupLocalCacheMap() {
        val mapOps = LocalCachedMapOptions
            .defaults<String, DeleteFileRequest>()
            .syncStrategy(LocalCachedMapOptions.SyncStrategy.UPDATE)
            .reconnectionStrategy(LocalCachedMapOptions.ReconnectionStrategy.NONE)

        this.cacheMap = this.redissonClient
            ?.getLocalCachedMap(
                "file:s3:deleted",
                TypedJsonJacksonCodec(String::class.java, DeleteFileRequest::class.java),
                mapOps
            )!!
    }

    @Test
    fun appServer1() {
        this.cacheMap["file:0"] = DeleteFileRequest("key1")
        this.cacheMap["file:1"] = DeleteFileRequest("key2")

        Flux
            .interval(Duration.ofSeconds(1))
            .doOnNext { println("File: ${this.cacheMap.get("file:${it % 2}")}") }
            .subscribe()

        Thread.sleep(60000)
    }

    @Test
    fun appServer2() {
        this.cacheMap["file:0"] = DeleteFileRequest("key1####")
    }
}