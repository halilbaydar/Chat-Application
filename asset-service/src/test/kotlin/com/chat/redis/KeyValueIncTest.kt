package com.chat.redis

import com.chat.common.BaseTest
import org.junit.jupiter.api.Test
import reactor.core.publisher.Flux
import reactor.test.StepVerifier
import java.time.Duration

class KeyValueIncTest : BaseTest() {

    @Test
    fun keyValueInc() {
        if (this.redissonReactiveClient != null) {
            val bucket = this.redissonReactiveClient!!.getAtomicLong("file:s3:process:delete:count")
            StepVerifier
                .create(Flux.range(0, 30)
                    .delayElements(Duration.ofSeconds(1))
                    .flatMap { bucket.incrementAndGet() }
                    .then()
                )
                .verifyComplete()
        }
    }
}