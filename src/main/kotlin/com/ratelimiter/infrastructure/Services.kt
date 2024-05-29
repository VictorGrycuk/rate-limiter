package com.ratelimiter.infrastructure

import com.ratelimiter.domain.notification.service.NotificationService
import com.ratelimiter.infrastructure.services.NotificationServiceImplementation
import org.kodein.di.DI
import org.kodein.di.bindSingleton

object Services {
    val di = DI {
        bindSingleton<NotificationService> { NotificationServiceImplementation() }
    }
}