package com.ratelimiter.infrastructure.repository

import com.ratelimiter.domain.notification.message.MessageType
import com.ratelimiter.infrastructure.services.ratelimiter.BaseRateLimiter
import com.ratelimiter.mothers.getRandomConfiguration
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class RateLimiterRepositoryTest {
    private val rateLimiterRepository = RateLimiterRepository()

    @Test
    fun `should return a rate limiter for each message type`() {
        MessageType.values().forEach {
            val config = getRandomConfiguration(messageType = it)

            val result = rateLimiterRepository.getRateLimiterHandler(config)

            assertThat(result).isInstanceOf(BaseRateLimiter::class.java)
        }
    }
}