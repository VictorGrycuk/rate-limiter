package com.ratelimiter.infrastructure.services.ratelimiter.strategy

import com.ratelimiter.infrastructure.http.exception.RateLimitedException
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class RemainingTokensValidationStrategyTest {
    private val remainingTokensValidationStrategy = RemainingTokensValidationStrategy()
    @Test
    fun `should throw rate limited exception when at or below limit`() {
        assertThrows<RateLimitedException> {
            remainingTokensValidationStrategy(0)
        }
    }

    @Test
    fun `should not throw rate limited exception when above limit`() {
        remainingTokensValidationStrategy(1)
    }
}