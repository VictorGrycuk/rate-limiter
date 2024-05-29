package com.ratelimiter.infrastructure.http.provider

import com.ratelimiter.action.SendNotificationAction
import com.ratelimiter.infrastructure.Services
import org.kodein.di.instance
import org.kodein.di.newInstance

object Actions {
    val sendNotification by Services.di.newInstance { SendNotificationAction(instance()) }
}