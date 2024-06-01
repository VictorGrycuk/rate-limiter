package com.ratelimiter.mothers

import com.ratelimiter.domain.notification.message.MessageType
import com.ratelimiter.infrastructure.services.configuration.RateLimiterConfig

fun getRandomConfiguration(
    messageType: MessageType = MessageType.values().random(),
    maxTokens: Int = getRandomInt(1, 5),
    schedule: String = "0 * * ? * * *",
) = RateLimiterConfig(
    messageType,
    maxTokens,
    schedule,
)