package com.chat.common

import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.TestInstance
import org.redisson.Redisson
import org.redisson.api.RedissonClient
import org.redisson.api.RedissonReactiveClient
import org.redisson.config.Config

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
abstract class BaseTest {
    protected var redissonReactiveClient: RedissonReactiveClient? = null
    protected var redissonClient: RedissonClient? = null

    @BeforeAll
    fun setup() {
        val config = Config()
        config.useSingleServer().setAddress("redis://localhost:6379")
        this.redissonReactiveClient = Redisson.create(config).reactive()
        this.redissonClient = Redisson.create(config)
    }

    @AfterAll
    fun shutDown() {
        this.redissonReactiveClient?.shutdown()
    }
}