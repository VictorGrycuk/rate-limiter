package com.ratelimiter.infrastructure.services.ratelimiter.fixedwindow

import com.ratelimiter.infrastructure.services.ratelimiter.strategy.RefillStrategy
import java.util.*
import java.util.concurrent.ConcurrentHashMap

class FixedWindowRefillStrategy: RefillStrategy {
    override fun invoke(map: ConcurrentHashMap<UUID, Int>) {
        map.clear()
    }
}