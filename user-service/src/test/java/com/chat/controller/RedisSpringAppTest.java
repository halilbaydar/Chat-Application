package com.chat.controller;

import com.chat.redis.RedisStorageManager;
import org.junit.jupiter.api.Test;
import org.redisson.api.RedissonReactiveClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@SpringBootTest
public class RedisSpringAppTest {
    @Autowired
    private RedisStorageManager redisStorageManager;
    @Autowired
    private RedissonReactiveClient redissonReactiveClient;

    @Test
    public void loadTest() {
        StepVerifier.create(
                Flux.range(0, 500_000)
                        .flatMap(index ->
                                redisStorageManager
                                        .value
                                        .increment("user:1:visit"))
                        .then()
        ).verifyComplete();
    }

    @Test
    public void redissonLoadTest() {
        var atomicLong = this.redissonReactiveClient.getAtomicLong("user:1:visit");
        StepVerifier.create(
                Flux.range(0, 500_000)
                        .flatMap(index -> atomicLong.incrementAndGet())
                        .then()
        ).verifyComplete();
    }
}
