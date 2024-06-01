package com.ratelimiter.infrastructure.services.ratelimiter

import com.ratelimiter.domain.notification.message.Notification
import com.ratelimiter.infrastructure.http.exception.RateLimitedException
import com.ratelimiter.infrastructure.services.configuration.RateLimiterConfig
import com.ratelimiter.infrastructure.services.ratelimiter.cor.Handler
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

class FixedWindowRateLimiter(
    private val configuration: RateLimiterConfig,
) : Handler {
    private val userRequests = ConcurrentHashMap<UUID, Int>()
    private val scheduler: ScheduledExecutorService = Executors.newScheduledThreadPool(1)
    init {
        reset()
    }

    private fun reset() {
        val initialDelay = resolveInitialDelay()

        scheduler.scheduleAtFixedRate({
            userRequests.clear()
        }, initialDelay, configuration.refillRate, TimeUnit.SECONDS)
    }

    private fun resolveInitialDelay(): Long {
        val now = LocalDateTime.now(ZoneId.systemDefault())
        val nextMidnight = now
            .toLocalDate()
            .plusDays(1)
            .atStartOfDay(ZoneId.systemDefault())

        return ChronoUnit.MILLIS.between(now, nextMidnight)
    }

    @Synchronized
    override fun check(message: Notification) {
        synchronized(this) {
            if (message.messageType != configuration.messageType)
                return
            val remainingRequests = userRequests[message.userId]
            if (remainingRequests != null) {
                validateRemainingRequests(remainingRequests)
                decreaseAllowedRequests(message)
            }
            else
                userRequests.put(message.userId, configuration.maxTokens.dec())
        }
    }

    private fun decreaseAllowedRequests(message: Notification) =
        userRequests[message.userId]!!.dec()

    private fun validateRemainingRequests(remainingRequests: Int) {
        if (remainingRequests == 0)
            throw RateLimitedException()
    }
}