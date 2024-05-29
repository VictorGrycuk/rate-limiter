package com.ratelimiter.action

import com.ratelimiter.domain.notification.message.Notification
import com.ratelimiter.domain.notification.service.NotificationService

class SendNotificationAction(
    private val notificationService: NotificationService
) {
    operator fun invoke(notification: Notification)
        = notificationService.send(notification)
}