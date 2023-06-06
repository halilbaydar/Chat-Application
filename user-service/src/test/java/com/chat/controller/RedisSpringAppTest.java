package com.chat.controller;

import com.chat.redis.RedisStorageManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@SpringBootTest
public class RedisSpringAppTest {
    @Autowired
    private RedisStorageManager redisStorageManager;

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
}
