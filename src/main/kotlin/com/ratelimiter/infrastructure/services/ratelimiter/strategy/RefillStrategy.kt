package com.ratelimiter.infrastructure.services.ratelimiter.strategy

import java.util.*
import java.util.concurrent.ConcurrentHashMap

interface RefillStrategy {
    operator fun invoke(map: ConcurrentHashMap<UUID, Int>)
}