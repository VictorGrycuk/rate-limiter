package com.ratelimiter.infrastructure

import com.ratelimiter.domain.notification.message.MessageType
import com.ratelimiter.domain.notification.service.NotificationService
import com.ratelimiter.domain.notification.service.RateLimiterService
import com.ratelimiter.infrastructure.services.NotificationServiceImplementation
import com.ratelimiter.infrastructure.services.configuration.Configuration
import com.ratelimiter.infrastructure.services.configuration.RateLimiterConfig
import com.ratelimiter.infrastructure.services.ratelimiter.cor.BucketRateLimiterHandler
import com.ratelimiter.infrastructure.services.ratelimiter.cor.RateLimiterImplementation
import com.sksamuel.hoplite.ConfigLoaderBuilder
import com.sksamuel.hoplite.addResourceSource
import org.kodein.di.DI
import org.kodein.di.bindSingleton
import org.kodein.di.instance

object Services {
    val configuration = ConfigLoaderBuilder.default()
        .addResourceSource("/config.yml")
        .build()
        .loadConfigOrThrow<Configuration>()

    val di = DI {
        bindSingleton<RateLimiterService> { getRateLimiter() }
        bindSingleton<NotificationService> { NotificationServiceImplementation(instance()) }
    }

    private fun getRateLimiter(): RateLimiterImplementation {
        val statusConfiguration = configuration.rateLimiter.getOrDefault(
            MessageType.STATUS,
            RateLimiterConfig(MessageType.STATUS, 2, 2, 60)
        )

        return RateLimiterImplementation(setOf(
            BucketRateLimiterHandler(statusConfiguration)
        ))
    }
}