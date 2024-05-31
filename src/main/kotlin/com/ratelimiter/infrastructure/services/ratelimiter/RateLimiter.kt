package com.ratelimiter.infrastructure.services.ratelimiter

interface RateLimiter {
    fun tryConsume(): Boolean
}