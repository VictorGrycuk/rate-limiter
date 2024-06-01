package com.ratelimiter.infrastructure.services.ratelimiter

import com.ratelimiter.domain.notification.message.Notification
import com.ratelimiter.infrastructure.services.configuration.RateLimiterConfig
import com.ratelimiter.infrastructure.services.ratelimiter.cor.RateLimiterHandler
import com.ratelimiter.infrastructure.services.ratelimiter.strategy.RateLimiterCheckStrategy
import com.ratelimiter.infrastructure.services.ratelimiter.strategy.SchedulerStrategy
import com.ratelimiter.infrastructure.services.ratelimiter.strategy.TokenValidationStrategy
import java.util.*
import java.util.concurrent.ConcurrentHashMap

class BaseRateLimiter(
    private val configuration: RateLimiterConfig,
    scheduler: SchedulerStrategy,
    private val isCorrectMessage: RateLimiterCheckStrategy,
    private val validateTokens: TokenValidationStrategy,
) : RateLimiterHandler {
    private val tokens = ConcurrentHashMap<UUID, Int>()
    init {
        scheduler(tokens)
    }

    override fun shouldHandleMessage(message: Notification) =
        this.isCorrectMessage(message)

    @Synchronized
    override fun check(message: Notification) {
        synchronized(this) {
            tokens.computeIfPresent(message.userId) { _, currentTokens ->
                validateTokens(currentTokens)
                currentTokens.dec()
            }
            tokens.computeIfAbsent(message.userId) { configuration.maxTokens.dec() }
        }
    }
}