package com.ratelimiter.infrastructure.services.ratelimiter

import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

class BucketRateLimiter(
    private val maxTokens: Long,
    private var initialTokens: Long,
    private val refillRate: Long,
) {
    private val scheduler: ScheduledExecutorService = Executors.newScheduledThreadPool(1)
    init {
        scheduler.scheduleAtFixedRate({
            synchronized(this) {
                initialTokens = minOf(maxTokens, initialTokens + refillRate)
            }
        }, refillRate, refillRate, TimeUnit.SECONDS)
    }

    @Synchronized
    fun tryConsume(count: Int = 1): Boolean {
        return if (initialTokens >= count) {
            initialTokens -= count
            true
        } else {
            false
        }
    }
}