package com.ratelimiter.infrastructure.http.exception

class InvalidNotificationTypeException(messageType: String):
    Exception("$messageType is not recognized as valid notification")