package com.ratelimiter.infrastructure.http.exception

class InvalidUserIdFormatTypeException(userId: String):
    Exception("$userId is not a valid UUID format")