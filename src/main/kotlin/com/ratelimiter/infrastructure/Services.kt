package com.ratelimiter.infrastructure

import com.ratelimiter.domain.notification.message.MessageType
import com.ratelimiter.domain.notification.service.NotificationService
import com.ratelimiter.domain.notification.service.RateLimiter
import com.ratelimiter.infrastructure.services.NotificationServiceImplementation
import com.ratelimiter.infrastructure.services.ratelimiter.BucketRateLimiter
import com.ratelimiter.infrastructure.services.ratelimiter.RateLimiterImplementation
import org.kodein.di.DI
import org.kodein.di.bindSingleton
import org.kodein.di.instance

object Services {
    val di = DI {
        bindSingleton<RateLimiter> { RateLimiterImplementation(mapOf(MessageType.STATUS to BucketRateLimiter(1, 5, 1))) }
        bindSingleton<NotificationService> { NotificationServiceImplementation(instance()) }
    }
}