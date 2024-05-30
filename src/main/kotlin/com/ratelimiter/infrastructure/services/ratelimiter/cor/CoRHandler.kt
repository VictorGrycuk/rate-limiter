package com.ratelimiter.infrastructure.services.ratelimiter.cor

import com.ratelimiter.domain.notification.message.Notification

interface CoRHandler {
    fun check(message: Notification)
}