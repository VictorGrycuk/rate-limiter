package com.ratelimiter.infrastructure.services.ratelimiter.bucket

import com.ratelimiter.infrastructure.services.configuration.RateLimiterConfig
import com.ratelimiter.infrastructure.services.ratelimiter.strategy.RefillStrategy
import com.ratelimiter.infrastructure.services.ratelimiter.strategy.SchedulerStrategy
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

class BucketSchedulerStrategy(
    private val configuration: RateLimiterConfig,
    private val refillStrategy: RefillStrategy,
): SchedulerStrategy {
    private val scheduler: ScheduledExecutorService = Executors.newScheduledThreadPool(1)
    override fun invoke(tokens: ConcurrentHashMap<UUID, Int>) {
        scheduler.scheduleAtFixedRate({
            synchronized(this) {
                refillStrategy(tokens)
            }
        }, configuration.refillRate, configuration.refillRate, TimeUnit.SECONDS)
    }
}