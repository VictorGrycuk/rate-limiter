package com.ratelimiter.infrastructure.http.client

class InvalidNotificationTypeException(messageType: String):
    Exception("$messageType is not recognized as valid notification")