package com.ratelimiter.infrastructure.services.ratelimiter.cor

import com.ratelimiter.infrastructure.http.exception.RateLimitedException
import com.ratelimiter.mothers.getRandomMessage
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.*

class RateLimiterImplementationTest {
    private val mockedStatusHandler: RateLimiterHandler = mock()
    private val mockedMarketingHandler: RateLimiterHandler = mock()
    @Test
    fun `should call handlers check when it should handle the message`() {
        val message = getRandomMessage()
        val rateLimiterImplementation = RateLimiterImplementation(setOf(mockedStatusHandler))
        whenever(
            mockedStatusHandler.shouldHandleMessage(any())
        ).thenReturn(true)

        rateLimiterImplementation.check(message)

        verify(mockedStatusHandler, times(1)).check(eq(message))
    }

    @Test
    fun `should call not handlers check when it should not handle the message`() {
        val message = getRandomMessage()
        val rateLimiterImplementation = RateLimiterImplementation(setOf(mockedStatusHandler))
        whenever(
            mockedStatusHandler.shouldHandleMessage(any())
        ).thenReturn(false)

        rateLimiterImplementation.check(message)

        verify(mockedStatusHandler, times(0)).check(eq(message))
    }

    @Test
    fun `should not call subsequent handlers when a previous raises a rate limited exception`() {
        val message = getRandomMessage()
        val rateLimiterImplementation = RateLimiterImplementation(setOf(mockedStatusHandler, mockedMarketingHandler))
        whenever(
            mockedStatusHandler.shouldHandleMessage(any())
        ).thenReturn(true)
        whenever(
            mockedStatusHandler.check(any())
        ).thenThrow(RateLimitedException())
        whenever(
            mockedMarketingHandler.shouldHandleMessage(any())
        ).thenReturn(true)

        assertThrows<RateLimitedException> {
            rateLimiterImplementation.check(message)
        }

        verify(mockedMarketingHandler, times(0)).check(eq(message))
    }
}