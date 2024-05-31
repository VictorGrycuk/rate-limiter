package com.ratelimiter.infrastructure.services.ratelimiter

import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

class BucketRateLimiter(
    private val maxTokens: Long,
    private var initialTokens: Long,
    refillRate: Long,
) : RateLimiter {
    private val scheduler: ScheduledExecutorService = Executors.newScheduledThreadPool(1)
    init {
        scheduler.scheduleAtFixedRate({
            synchronized(this) {
                initialTokens = maxTokens
            }
        }, refillRate, refillRate, TimeUnit.SECONDS)
    }

    @Synchronized
    override fun tryConsume(): Boolean {
        return if (initialTokens >= 1) {
            initialTokens -= 1
            true
        } else {
            false
        }
    }
}