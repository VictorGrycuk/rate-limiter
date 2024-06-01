package com.ratelimiter.infrastructure.services.ratelimiter.cor

import com.ratelimiter.domain.notification.message.Notification

interface Handler {
    fun check(message: Notification)
}