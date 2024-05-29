package com.ratelimiter.infrastructure.http.handler

import com.ratelimiter.domain.notification.message.MessageType
import com.ratelimiter.infrastructure.http.client.InvalidNotificationTypeException
import com.ratelimiter.infrastructure.http.client.InvalidUserIdFormatTypeException
import com.ratelimiter.mothers.getRandomString
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.*

class NotificationDTOTest {
    @Test
    fun `should map to notification`() {
        val notificationDTO = NotificationDTO(
            messageType = MessageType.values().random().toString(),
            userId = UUID.randomUUID().toString(),
            message = getRandomString(10),
        )

        val result = notificationDTO.toNotification()

        assertThat(result.messageType).isEqualTo(MessageType.valueOf(notificationDTO.messageType))
        assertThat(result.userId.toString()).isEqualTo(notificationDTO.userId)
        assertThat(result.message).isEqualTo(notificationDTO.message)
    }

    @Test
    fun `should throw invalid notification type exception when message type is invalid`() {
        val invalidNotificationType = getRandomString(15)

        val notificationDTO = NotificationDTO(
            messageType = invalidNotificationType,
            userId = UUID.randomUUID().toString(),
            message = getRandomString(10),
        )

        assertThrows<InvalidNotificationTypeException> {
            notificationDTO.toNotification()
        }
    }

    @Test
    fun `should throw invalid user id format exception when user id is invalid`() {
        val invalidUserId = getRandomString(15)

        val notificationDTO = NotificationDTO(
            messageType = MessageType.values().random().toString(),
            userId = invalidUserId,
            message = getRandomString(10),
        )

        assertThrows<InvalidUserIdFormatTypeException> {
            notificationDTO.toNotification()
        }
    }
}