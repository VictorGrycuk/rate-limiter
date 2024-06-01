package com.ratelimiter.infrastructure

import com.ratelimiter.domain.notification.service.NotificationService
import com.ratelimiter.domain.notification.service.RateLimiterService
import com.ratelimiter.infrastructure.repository.RateLimiterRepository
import com.ratelimiter.infrastructure.services.NotificationServiceImplementation
import com.ratelimiter.infrastructure.services.configuration.Configuration
import com.ratelimiter.infrastructure.services.ratelimiter.cor.RateLimiterImplementation
import com.sksamuel.hoplite.ConfigLoaderBuilder
import com.sksamuel.hoplite.addResourceSource
import org.kodein.di.DI
import org.kodein.di.bindSingleton
import org.kodein.di.instance

object Services {
    private val configuration = ConfigLoaderBuilder.default()
        .addResourceSource("/config.yml")
        .build()
        .loadConfigOrThrow<Configuration>()

    private val rateLimiterRepository = RateLimiterRepository()

    val di = DI {
        bindSingleton<RateLimiterService> { getRateLimiter() }
        bindSingleton<NotificationService> { NotificationServiceImplementation(instance()) }
    }

    private fun getRateLimiter(): RateLimiterImplementation {
        val rateLimiters = configuration.rateLimiter
            .map { (_, value) -> rateLimiterRepository.getRateLimiterHandler(value) }
            .toSet()

        return RateLimiterImplementation(rateLimiters)
    }
}