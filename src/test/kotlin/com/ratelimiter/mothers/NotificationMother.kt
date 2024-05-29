package com.ratelimiter.mothers

import com.ratelimiter.domain.notification.message.MessageType
import com.ratelimiter.domain.notification.message.Notification
import java.util.*

fun getRandomMessage(
    messageType: MessageType = MessageType.values().random(),
    userId: UUID = UUID.randomUUID(),
    message: String = getRandomString()
) = Notification(
    messageType,
    userId,
    message
)