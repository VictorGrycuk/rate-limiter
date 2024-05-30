package com.ratelimiter.infrastructure.services.ratelimiter.cor

import com.ratelimiter.domain.notification.message.Notification
import com.ratelimiter.domain.notification.service.RateLimiter

class RateLimiterImplementation(
    private var handlers: Set<CoRHandler>
): RateLimiter {
    override fun check(notification: Notification) {
        handlers.forEach { it.check(notification) }
    }
}