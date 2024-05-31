package com.ratelimiter.infrastructure.services

import com.ratelimiter.domain.notification.message.Notification
import com.ratelimiter.domain.notification.service.NotificationService
import com.ratelimiter.domain.notification.service.RateLimiterService

class NotificationServiceImplementation(
    private val rateLimiter: RateLimiterService
): NotificationService {
    override fun send(notification: Notification) {
        rateLimiter.check(notification)
        println(notification)
    }
}