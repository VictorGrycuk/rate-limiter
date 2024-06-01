package com.ratelimiter.infrastructure.services.ratelimiter.strategy

import java.util.*
import java.util.concurrent.ConcurrentHashMap

interface SchedulerStrategy {
    operator fun invoke(tokens: ConcurrentHashMap<UUID, Int>)
}