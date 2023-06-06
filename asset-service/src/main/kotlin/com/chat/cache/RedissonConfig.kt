package com.chat.cache

import org.redisson.Redisson
import org.redisson.api.RedissonClient
import org.redisson.api.RedissonReactiveClient
import org.redisson.config.Config
import org.springframework.boot.autoconfigure.data.redis.RedisProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class RedissonConfig(private val props: RedisProperties) {

    @Bean
    fun redissonClient(): RedissonClient {
        val config = Config()
        config.useSingleServer().setAddress("redis://${props.host}:${props.port}")
        return Redisson.create(config)
    }

    @Bean
    fun redissonReactiveClient(redissonClient: RedissonClient): RedissonReactiveClient {
        return redissonClient.reactive()
    }
}