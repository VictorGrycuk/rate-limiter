package com.ratelimiter.infrastructure.services.ratelimiter

import com.ratelimiter.domain.notification.message.Notification
import com.ratelimiter.infrastructure.http.exception.RateLimitedException
import com.ratelimiter.infrastructure.services.configuration.RateLimiterConfig
import com.ratelimiter.infrastructure.services.ratelimiter.cor.Handler
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

class BucketRateLimiter(
    private val configuration: RateLimiterConfig,
) : Handler {
    private val tokens = ConcurrentHashMap<UUID, Int>()
    private val scheduler: ScheduledExecutorService = Executors.newScheduledThreadPool(1)
    init {
        scheduler.scheduleAtFixedRate({
            synchronized(this) {
                tokens
                    .filterValues { it < configuration.maxTokens }
                    .forEach { (key, _) -> tokens[key] = configuration.maxTokens }
            }
        }, configuration.refillRate, configuration.refillRate, TimeUnit.SECONDS)
    }

    @Synchronized
    override fun check(message: Notification) {
        synchronized(this) {
            if (message.messageType != configuration.messageType)
                return

            val remainingTokens = tokens[message.userId]
            if (remainingTokens != null) {
                validateRemainingTokens(remainingTokens)
                decreaseTokens(message)
            }
            else
                tokens.put(message.userId, configuration.maxTokens.dec())
        }
    }

    private fun validateRemainingTokens(remainingTokens: Int) {
        if (remainingTokens == 0)
            throw RateLimitedException()
    }

    private fun decreaseTokens(message: Notification) {
        tokens[message.userId] = tokens[message.userId]!!.dec()
    }
}