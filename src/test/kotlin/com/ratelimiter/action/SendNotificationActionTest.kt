package com.ratelimiter.action

import com.ratelimiter.domain.notification.service.NotificationService
import com.ratelimiter.mothers.getRandomMessage
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

class SendNotificationActionTest {
    private val notificationServiceMock: NotificationService = mock()
    private val sendNotificationAction = SendNotificationAction(notificationServiceMock)

    @Test
    fun `should invoke notification service`() {
        val message = getRandomMessage()

        sendNotificationAction(message)

        verify(notificationServiceMock).send(message)
    }
}