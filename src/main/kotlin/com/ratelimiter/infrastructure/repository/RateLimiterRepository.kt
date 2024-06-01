package com.ratelimiter.infrastructure.repository

import com.ratelimiter.infrastructure.services.configuration.RateLimitedKind
import com.ratelimiter.infrastructure.services.configuration.RateLimiterConfig
import com.ratelimiter.infrastructure.services.ratelimiter.BaseRateLimiter
import com.ratelimiter.infrastructure.services.ratelimiter.cor.RateLimiterHandler
import com.ratelimiter.infrastructure.services.ratelimiter.strategy.*

class RateLimiterRepository {
    fun getRateLimiterHandler(config: RateLimiterConfig): RateLimiterHandler {
        return when(config.kind) {
            RateLimitedKind.BUCKET -> getBucketRateLimiter(config)
            RateLimitedKind.FIXED_WINDOW -> getFixedWindowRateLimiter(config)
        }
    }

    private fun getBucketRateLimiter(config: RateLimiterConfig) = BaseRateLimiter(
        config,
        SchedulerStrategyImplementation(
            config,
            RefillBucketStrategy::class.java
        ),
        MessageTypeCheckStrategy(config),
        RemainingTokensValidationStrategy()
    )

    private fun getFixedWindowRateLimiter(config: RateLimiterConfig) = BaseRateLimiter(
        config,
        SchedulerStrategyImplementation(
            config,
            FixedWindowRefillStrategy::class.java
        ),
        MessageTypeCheckStrategy(config),
        RemainingTokensValidationStrategy()
    )
}