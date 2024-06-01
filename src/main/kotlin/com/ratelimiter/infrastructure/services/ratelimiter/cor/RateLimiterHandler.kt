package com.ratelimiter.infrastructure.services.ratelimiter.cor

import com.ratelimiter.domain.notification.message.Notification

interface RateLimiterHandler {
    fun shouldHandleMessage(message: Notification): Boolean
    fun check(message: Notification)
}