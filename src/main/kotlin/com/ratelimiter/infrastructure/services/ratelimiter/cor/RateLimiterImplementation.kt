package com.ratelimiter.infrastructure.services.ratelimiter.cor

import com.ratelimiter.domain.notification.message.Notification
import com.ratelimiter.domain.notification.service.RateLimiterService

class RateLimiterImplementation(
    private var handlers: Set<CoRHandler>
): RateLimiterService {
    override fun check(notification: Notification) {
        handlers.forEach { it.check(notification) }
    }
}