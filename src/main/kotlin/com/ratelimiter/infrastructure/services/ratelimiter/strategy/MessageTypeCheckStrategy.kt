package com.ratelimiter.infrastructure.services.ratelimiter.strategy

import com.ratelimiter.domain.notification.message.Notification
import com.ratelimiter.infrastructure.services.configuration.RateLimiterConfig

class MessageTypeCheckStrategy(
    private val configuration: RateLimiterConfig,
): RateLimiterCheckStrategy {
    override fun invoke(message: Notification) =
        message.messageType == configuration.messageType
}