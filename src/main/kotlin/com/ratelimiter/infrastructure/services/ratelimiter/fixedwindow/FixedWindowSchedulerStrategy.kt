package com.ratelimiter.infrastructure.services.ratelimiter.fixedwindow

import com.ratelimiter.infrastructure.services.configuration.RateLimiterConfig
import com.ratelimiter.infrastructure.services.ratelimiter.strategy.RefillStrategy
import com.ratelimiter.infrastructure.services.ratelimiter.strategy.SchedulerStrategy
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

class FixedWindowSchedulerStrategy(
    private val configuration: RateLimiterConfig,
    private val refillStrategy: RefillStrategy,
): SchedulerStrategy {
    private val scheduler: ScheduledExecutorService = Executors.newScheduledThreadPool(1)
    override fun invoke(tokens: ConcurrentHashMap<UUID, Int>) {
        val initialDelay = resolveRemainingTimeUntilMidnight()
        scheduler.scheduleAtFixedRate({
            refillStrategy(tokens)
        }, initialDelay, configuration.refillRate, TimeUnit.SECONDS)
    }

    private fun resolveRemainingTimeUntilMidnight(): Long {
        val now = LocalDateTime.now(ZoneId.systemDefault())
        val nextMidnight = now
            .toLocalDate()
            .plusDays(1)
            .atStartOfDay(ZoneId.systemDefault())

        return ChronoUnit.MILLIS.between(now, nextMidnight)
    }
}