package com.ratelimiter.infrastructure.services

import com.ratelimiter.domain.notification.message.Notification
import com.ratelimiter.domain.notification.service.RateLimiter
import com.ratelimiter.infrastructure.http.exception.RateLimitedException

class RateLimiterImplementation(
    private val maxMessages: Int = 2
): RateLimiter {
    private var currentMessagesSent = 0
    override fun check(notification: Notification) {
        if (currentMessagesSent >= maxMessages)
            throw RateLimitedException()

        currentMessagesSent++
    }
}