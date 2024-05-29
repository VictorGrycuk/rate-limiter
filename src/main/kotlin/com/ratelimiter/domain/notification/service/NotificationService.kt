package com.ratelimiter.domain.notification.service

import com.ratelimiter.domain.notification.message.Notification

interface NotificationService {
    fun send(notification: Notification)
}