package com.ratelimiter.infrastructure.services.configuration

import com.ratelimiter.domain.notification.message.MessageType

data class RateLimiterConfig(
    val messageType: MessageType,
    val kind: RateLimitedKind,
    val maxTokens: Int,
    val schedule: String
)