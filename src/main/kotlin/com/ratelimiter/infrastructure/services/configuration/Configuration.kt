package com.ratelimiter.infrastructure.services.configuration

import com.ratelimiter.domain.notification.message.MessageType

data class Configuration(
    val rateLimiter: Map<MessageType, RateLimiterConfig>
)