package com.ratelimiter.infrastructure.services.ratelimiter

import com.ratelimiter.domain.notification.message.MessageType
import com.ratelimiter.domain.notification.message.Notification
import com.ratelimiter.domain.notification.service.RateLimiter
import com.ratelimiter.infrastructure.http.exception.RateLimitedException

class RateLimiterImplementation(
    private val buckets: Map<MessageType, BucketRateLimiter>
): RateLimiter {
    override fun check(notification: Notification) {
        if (buckets.containsKey(notification.messageType).not())
            return

        val bucket = buckets[notification.messageType]!!

        if (bucket.tryConsume().not())
            throw RateLimitedException()
    }
}