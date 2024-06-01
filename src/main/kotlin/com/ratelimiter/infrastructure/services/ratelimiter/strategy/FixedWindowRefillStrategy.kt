package com.ratelimiter.infrastructure.services.ratelimiter.strategy

import org.quartz.Job
import org.quartz.JobExecutionContext
import java.util.concurrent.ConcurrentHashMap

class FixedWindowRefillStrategy: Job {
    override fun execute(context: JobExecutionContext?) {
        val result = context?.jobDetail?.jobDataMap?.get("tokens")
        (result as ConcurrentHashMap<*, *>).clear()
    }
}