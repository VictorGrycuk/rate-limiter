package com.ratelimiter.domain.notification.message

import java.util.UUID

data class Notification(
    var messageType: MessageType,
    var userId: UUID,
    var message: String,
)

