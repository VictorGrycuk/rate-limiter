package com.ratelimiter.infrastructure.services.ratelimiter.strategy

import com.ratelimiter.domain.notification.message.MessageType
import com.ratelimiter.mothers.getRandomConfiguration
import com.ratelimiter.mothers.getRandomMessage
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class MessageTypeCheckStrategyTest {
    @Test
    fun `should return true when the message type matches with configuration` () {
        val message = getRandomMessage()
        val configuration = getRandomConfiguration(message.messageType)
        val messageTypeCheckStrategy = MessageTypeCheckStrategy(configuration)

        val result = messageTypeCheckStrategy(message)

        assertThat(result).isTrue()
    }

    @Test
    fun `should return false when the message type does not match with configuration` () {
        val message = getRandomMessage(MessageType.STATUS)
        val configuration = getRandomConfiguration(MessageType.MARKETING)
        val messageTypeCheckStrategy = MessageTypeCheckStrategy(configuration)

        val result = messageTypeCheckStrategy(message)

        assertThat(result).isFalse()
    }
}