package com.ratelimiter.infrastructure

import com.ratelimiter.domain.notification.message.MessageType
import com.ratelimiter.domain.notification.service.NotificationService
import com.ratelimiter.domain.notification.service.RateLimiterService
import com.ratelimiter.infrastructure.services.NotificationServiceImplementation
import com.ratelimiter.infrastructure.services.configuration.Configuration
import com.ratelimiter.infrastructure.services.configuration.RateLimiterConfig
import com.ratelimiter.infrastructure.services.ratelimiter.BaseRateLimiter
import com.ratelimiter.infrastructure.services.ratelimiter.bucket.BucketSchedulerStrategy
import com.ratelimiter.infrastructure.services.ratelimiter.bucket.RefillBucketStrategy
import com.ratelimiter.infrastructure.services.ratelimiter.cor.RateLimiterImplementation
import com.ratelimiter.infrastructure.services.ratelimiter.fixedwindow.FixedWindowRefillStrategy
import com.ratelimiter.infrastructure.services.ratelimiter.fixedwindow.FixedWindowSchedulerStrategy
import com.ratelimiter.infrastructure.services.ratelimiter.strategy.MessageTypeCheckStrategy
import com.ratelimiter.infrastructure.services.ratelimiter.strategy.RemainingTokensValidationStrategy
import com.sksamuel.hoplite.ConfigLoaderBuilder
import com.sksamuel.hoplite.addResourceSource
import org.kodein.di.DI
import org.kodein.di.bindSingleton
import org.kodein.di.instance
import java.util.concurrent.TimeUnit

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
            RateLimiterConfig(MessageType.STATUS, 2, 2, TimeUnit.MINUTES.toSeconds(1))
        )
        val marketingConfiguration = configuration.rateLimiter.getOrDefault(
            MessageType.MARKETING,
            RateLimiterConfig(MessageType.MARKETING, 3, 3, TimeUnit.HOURS.toSeconds(1))
        )
        val newsConfiguration = configuration.rateLimiter.getOrDefault(
            MessageType.NEWS,
            RateLimiterConfig(MessageType.NEWS, 1, 1, TimeUnit.DAYS.toSeconds(1))
        )

        val statusRateLimiter = BaseRateLimiter(
            statusConfiguration,
            BucketSchedulerStrategy(
                statusConfiguration,
                RefillBucketStrategy(statusConfiguration)),
            MessageTypeCheckStrategy(statusConfiguration),
            RemainingTokensValidationStrategy()
        )

        val marketingRateLimiter = BaseRateLimiter(
            marketingConfiguration,
            BucketSchedulerStrategy(
                marketingConfiguration,
                RefillBucketStrategy(marketingConfiguration)),
            MessageTypeCheckStrategy(marketingConfiguration),
            RemainingTokensValidationStrategy()
        )

        val newsRateLimiter = BaseRateLimiter(
            newsConfiguration,
            FixedWindowSchedulerStrategy(
                newsConfiguration,
                FixedWindowRefillStrategy()),
            MessageTypeCheckStrategy(newsConfiguration),
            RemainingTokensValidationStrategy()
        )

        return RateLimiterImplementation(setOf(
            statusRateLimiter,
            marketingRateLimiter,
            newsRateLimiter,
        ))
    }
}