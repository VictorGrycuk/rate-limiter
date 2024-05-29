package com.ratelimiter.infrastructure.services.ratelimiter

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.lang.Thread.sleep

class BucketRateLimiterTest {
    @Test
    fun `should return true if there are enough tokens`() {
        val bucketRateLimiter = BucketRateLimiter(
            1,
            1,
            10
        )

        val result = bucketRateLimiter.tryConsume()

        assertTrue(result)
    }

    @Test
    fun `should refill tokens`() {
        val bucketRateLimiter = BucketRateLimiter(
            1,
            1,
            1
        )

        bucketRateLimiter.tryConsume()
        sleep(1500)
        val result = bucketRateLimiter.tryConsume()

        assertTrue(result)
    }

    @Test
    fun `should return false after consuming all tokens`() {
        val bucketRateLimiter = BucketRateLimiter(
            1,
            1,
            10
        )

        var result = true
        repeat(2) {
            result = bucketRateLimiter.tryConsume()
        }

        assertFalse(result)
    }
}