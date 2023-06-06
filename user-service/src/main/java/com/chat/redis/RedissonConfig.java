package com.chat.redis;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.api.RedissonReactiveClient;
import org.redisson.codec.TypedJsonJacksonCodec;
import org.redisson.config.Config;
import org.redisson.spring.cache.RedissonSpringCacheManager;
import org.redisson.spring.data.connection.RedissonConnectionFactory;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;

import java.util.Set;

@Configuration
public class RedissonConfig {

    @Bean
    public RedissonReactiveClient redissonReactiveClient(RedissonClient redissonClient) {
        return redissonClient.reactive();
    }

    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config();
        config.useMasterSlaveServers().setMasterAddress("redis://localhost:6379");
        config.useMasterSlaveServers().setSlaveAddresses(Set.of("redis://localhost:6380"));
        config.useMasterSlaveServers().setDatabase(0);
        config.setCodec(new TypedJsonJacksonCodec(Object.class));
        return Redisson.create(config);
    }

    @Bean
    public ReactiveRedisConnectionFactory redisConnectionFactory(RedissonClient redissonClient) {
        return new RedissonConnectionFactory(redissonClient);
    }

    @Bean
    public CacheManager cacheManager(RedissonClient redissonClient) {
        return new RedissonSpringCacheManager(redissonClient);
    }
}
