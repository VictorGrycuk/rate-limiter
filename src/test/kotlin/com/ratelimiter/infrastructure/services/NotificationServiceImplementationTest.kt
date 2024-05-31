package com.ratelimiter.infrastructure.services

import com.ratelimiter.domain.notification.service.RateLimiterService
import com.ratelimiter.infrastructure.http.exception.RateLimitedException
import com.ratelimiter.mothers.getRandomMessage
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class NotificationServiceImplementationTest {
    private val rateLimiter: RateLimiterService = mock()
    private val notificationService = NotificationServiceImplementation(rateLimiter)

    @Test
    fun `should send message if not rate limited`() {
        val message = getRandomMessage()

        notificationService.send(message)
    }

    @Test
    fun `should not send message when it is rate limited`() {
        val message = getRandomMessage()
        whenever(rateLimiter.check(any())).thenThrow(RateLimitedException())

        assertThrows<RateLimitedException> {
            notificationService.send(message)
        }
    }
}