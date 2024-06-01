package com.ratelimiter.infrastructure.services.ratelimiter.cor

import com.ratelimiter.domain.notification.message.Notification
import com.ratelimiter.domain.notification.service.RateLimiterService

class RateLimiterImplementation(
    private var rateLimiterHandlers: Set<RateLimiterHandler>
): RateLimiterService {
    override fun check(notification: Notification) {
        rateLimiterHandlers
            .filter { it.shouldHandleMessage(notification) }
            .forEach { it.check(notification) }
    }
}