package com.ratelimiter.infrastructure

import com.ratelimiter.domain.notification.service.NotificationService
import com.ratelimiter.domain.notification.service.RateLimiter
import com.ratelimiter.infrastructure.services.NotificationServiceImplementation
import com.ratelimiter.infrastructure.services.ratelimiter.cor.RateLimiterImplementation
import com.ratelimiter.infrastructure.services.ratelimiter.cor.RateLimiterHandler
import org.kodein.di.DI
import org.kodein.di.bindSingleton
import org.kodein.di.instance

object Services {
    val di = DI {
        bindSingleton<RateLimiter> { getRateLimiter() }
        bindSingleton<NotificationService> { NotificationServiceImplementation(instance()) }
    }

    private fun getRateLimiter() = RateLimiterImplementation(setOf(
            RateLimiterHandler()
        )
    )
}