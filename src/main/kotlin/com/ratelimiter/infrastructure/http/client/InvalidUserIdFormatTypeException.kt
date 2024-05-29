package com.ratelimiter.infrastructure.http.client

class InvalidUserIdFormatTypeException(userId: String):
    Exception("$userId is not a valid UUID format")