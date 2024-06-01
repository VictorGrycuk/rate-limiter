package com.ratelimiter.infrastructure.services.ratelimiter.bucket

import com.ratelimiter.infrastructure.services.configuration.RateLimiterConfig
import com.ratelimiter.infrastructure.services.ratelimiter.strategy.RefillStrategy
import java.util.*
import java.util.concurrent.ConcurrentHashMap

class RefillBucketStrategy(
    private val configuration: RateLimiterConfig,
): RefillStrategy {
    override fun invoke(map: ConcurrentHashMap<UUID, Int>) {
        map.filterValues { it < configuration.maxTokens }
            .forEach { (key, _) -> map[key] = configuration.maxTokens }
    }
}