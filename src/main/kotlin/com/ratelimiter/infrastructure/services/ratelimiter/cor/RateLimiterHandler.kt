package com.ratelimiter.infrastructure.services.ratelimiter.cor

import com.ratelimiter.domain.notification.message.Notification

interface RateLimiterHandler {
    fun check(message: Notification)
}