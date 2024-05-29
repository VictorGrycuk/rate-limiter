package com.ratelimiter.infrastructure.services

import com.ratelimiter.domain.notification.message.Notification
import com.ratelimiter.domain.notification.service.NotificationService

class NotificationServiceImplementation: NotificationService {
    override fun send(notification: Notification) {
        println(notification)
    }
}