package com.ratelimiter.infrastructure.services.ratelimiter.cor

import com.ratelimiter.domain.notification.message.MessageType
import com.ratelimiter.domain.notification.message.Notification
import com.ratelimiter.infrastructure.http.exception.RateLimitedException
import com.ratelimiter.infrastructure.services.ratelimiter.BucketRateLimiter
import java.util.*

class RateLimiterHandler(
    private var map: Map<UUID, BucketRateLimiter> = mapOf()
): CoRHandler {
    override fun check(message: Notification) {
        if (message.messageType != MessageType.STATUS)
            return

        if(map.containsKey(message.userId).not())
            map = map.plus(message.userId to BucketRateLimiter(2, 2, 60))

        if(map[message.userId]!!.tryConsume().not())
            throw RateLimitedException()
    }
}