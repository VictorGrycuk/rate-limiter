package com.ratelimiter.infrastructure.services.ratelimiter.strategy

import com.ratelimiter.infrastructure.services.configuration.RateLimiterConfig
import org.quartz.Job
import org.quartz.JobExecutionContext
import java.util.*
import java.util.concurrent.ConcurrentHashMap

class RefillBucketStrategy: Job {
    override fun execute(context: JobExecutionContext?) {
        val map = context?.jobDetail?.jobDataMap?.get("tokens") as ConcurrentHashMap<UUID, Int>
        val config = context.jobDetail?.jobDataMap?.get("configuration") as RateLimiterConfig
        map.filterValues { it < config.maxTokens }
            .forEach { (key, _) -> map[key] = config.maxTokens }
    }
}