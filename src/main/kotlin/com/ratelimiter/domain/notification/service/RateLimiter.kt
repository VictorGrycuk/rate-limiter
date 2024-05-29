package com.ratelimiter.domain.notification.service

import com.ratelimiter.domain.notification.message.Notification

interface RateLimiter {
    fun check(notification: Notification)
}