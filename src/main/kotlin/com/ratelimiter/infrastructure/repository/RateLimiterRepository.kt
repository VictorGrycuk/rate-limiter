package com.ratelimiter.infrastructure.repository

import com.ratelimiter.domain.notification.message.MessageType
import com.ratelimiter.infrastructure.services.configuration.RateLimiterConfig
import com.ratelimiter.infrastructure.services.ratelimiter.BaseRateLimiter
import com.ratelimiter.infrastructure.services.ratelimiter.cor.RateLimiterHandler
import com.ratelimiter.infrastructure.services.ratelimiter.strategy.*

class RateLimiterRepository {
    fun getRateLimiterHandler(config: RateLimiterConfig): RateLimiterHandler {
        return when(config.messageType) {
            MessageType.STATUS,
            MessageType.MARKETING -> getBucketRateLimiter(config)
            MessageType.NEWS -> getFixedWindowRateLimiter(config)
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