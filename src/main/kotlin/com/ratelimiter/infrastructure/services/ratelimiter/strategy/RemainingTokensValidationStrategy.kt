package com.ratelimiter.infrastructure.services.ratelimiter.strategy

import com.ratelimiter.infrastructure.http.exception.RateLimitedException

class RemainingTokensValidationStrategy: TokenValidationStrategy {
    override fun invoke(tokens: Int) {
        if (tokens <= 0)
            throw RateLimitedException()
    }
}