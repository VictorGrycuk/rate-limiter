package com.ratelimiter.infrastructure.http.handler

import com.ratelimiter.domain.notification.message.MessageType
import com.ratelimiter.domain.notification.message.Notification
import com.ratelimiter.infrastructure.http.exception.InvalidNotificationTypeException
import com.ratelimiter.infrastructure.http.exception.InvalidUserIdFormatTypeException
import java.util.*

data class NotificationDTO(
    var messageType: String,
    var userId: String,
    var message: String,
) {
    fun toNotification() = Notification(
        messageType = resolveNotificationType(),
        userId = resolveUUID(),
        message = message
    )

    private fun resolveNotificationType(): MessageType {
        return try {
            MessageType.valueOf(messageType)
        } catch (exception: Exception) {
            throw InvalidNotificationTypeException(messageType)
        }
    }

    private fun resolveUUID(): UUID {
        return try {
            UUID.fromString(userId)
        } catch (exception: Exception) {
            throw InvalidUserIdFormatTypeException(userId)
        }
    }
}
