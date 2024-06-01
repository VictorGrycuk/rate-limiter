package com.ratelimiter.infrastructure.services.ratelimiter.cor

import com.ratelimiter.domain.notification.message.MessageType
import com.ratelimiter.infrastructure.http.exception.RateLimitedException
import com.ratelimiter.infrastructure.services.configuration.RateLimiterConfig
import com.ratelimiter.infrastructure.services.ratelimiter.BucketRateLimiter
import com.ratelimiter.mothers.getRandomMessage
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class BucketRateLimiterHandlerTest {
    @Test
    fun `should throw exception when rate limiting`() {
        val configuration = RateLimiterConfig(MessageType.STATUS, 2, 2, 10)
        val message = getRandomMessage(MessageType.STATUS)

        val bucketRateLimiter = BucketRateLimiter(configuration)

        repeat(2) { bucketRateLimiter.check(message) }

        assertThrows<RateLimitedException> {
            bucketRateLimiter.check(message)
        }
    }

    @Test
    fun `should not rate limit when message does not match`() {
        val configuration = RateLimiterConfig(MessageType.STATUS, 1, 1, 10)
        val message = getRandomMessage(MessageType.MARKETING)

        val bucketRateLimiter = BucketRateLimiter(configuration)

        repeat(2) { bucketRateLimiter.check(message) }
    }
}