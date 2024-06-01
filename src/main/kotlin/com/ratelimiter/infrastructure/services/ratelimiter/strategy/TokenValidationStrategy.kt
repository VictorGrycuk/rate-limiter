package com.ratelimiter.infrastructure.services.ratelimiter.strategy

interface TokenValidationStrategy {
    operator fun invoke(tokens: Int)
}