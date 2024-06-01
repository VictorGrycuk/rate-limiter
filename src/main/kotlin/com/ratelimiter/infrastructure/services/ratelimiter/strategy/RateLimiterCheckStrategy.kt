package com.ratelimiter.infrastructure.services.ratelimiter.strategy

import com.ratelimiter.domain.notification.message.Notification

interface RateLimiterCheckStrategy {
    operator fun invoke(message: Notification): Boolean
}