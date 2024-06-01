package com.ratelimiter.infrastructure.services.ratelimiter

import com.ratelimiter.domain.notification.message.Notification
import com.ratelimiter.infrastructure.services.configuration.RateLimiterConfig
import com.ratelimiter.infrastructure.services.ratelimiter.cor.Handler
import com.ratelimiter.infrastructure.services.ratelimiter.strategy.RateLimiterCheckStrategy
import com.ratelimiter.infrastructure.services.ratelimiter.strategy.SchedulerStrategy
import com.ratelimiter.infrastructure.services.ratelimiter.strategy.TokenValidationStrategy
import java.util.*
import java.util.concurrent.ConcurrentHashMap

class BaseRateLimiter(
    private val configuration: RateLimiterConfig,
    scheduler: SchedulerStrategy,
    private val shouldSkip: RateLimiterCheckStrategy,
    private val validateTokens: TokenValidationStrategy,
) : Handler {
    private val tokens = ConcurrentHashMap<UUID, Int>()
    init {
        scheduler(tokens)
    }

    @Synchronized
    override fun check(message: Notification) {
        synchronized(this) {
            if (shouldSkip(message))
                return

            tokens.computeIfPresent(message.userId) { _, currentTokens ->
                validateTokens(currentTokens)
                currentTokens.dec()
            }
            tokens.computeIfAbsent(message.userId) { configuration.maxTokens.dec() }
        }
    }
}