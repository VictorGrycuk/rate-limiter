package com.ratelimiter.infrastructure.services.ratelimiter.cor

import com.ratelimiter.domain.notification.message.Notification
import com.ratelimiter.infrastructure.http.exception.RateLimitedException
import com.ratelimiter.infrastructure.services.configuration.RateLimiterConfig
import com.ratelimiter.infrastructure.services.ratelimiter.BucketRateLimiter
import com.ratelimiter.infrastructure.services.ratelimiter.RateLimiter
import java.util.*

class BucketRateLimiterHandler(
    private val configuration: RateLimiterConfig,
    private var map: Map<UUID, RateLimiter> = mapOf()
): CoRHandler {
    override fun check(message: Notification) {
        if (message.messageType != configuration.messageType)
            return

        if(isUserIdMissing(message))
            addBucketForUser(message.userId)

        if(isRateLimited(message))
            throw RateLimitedException()
    }

    private fun isUserIdMissing(message: Notification) =
        map.containsKey(message.userId).not()

    private fun addBucketForUser(userId: UUID) {
        map = map.plus(userId to getNewBucket())
    }

    private fun getNewBucket() = BucketRateLimiter(
        configuration.maxTokens,
        configuration.initialTokens,
        configuration.refillRate
    )

    private fun isRateLimited(message: Notification) =
        map[message.userId]!!.tryConsume().not()
}
