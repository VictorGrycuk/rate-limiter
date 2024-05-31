package com.ratelimiter.domain.notification.service

import com.ratelimiter.domain.notification.message.Notification

interface RateLimiterService {
    fun check(notification: Notification)
}