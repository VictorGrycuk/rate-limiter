package com.ratelimiter.infrastructure.services.ratelimiter.strategy

import com.ratelimiter.infrastructure.services.configuration.RateLimiterConfig
import org.quartz.*
import org.quartz.impl.StdSchedulerFactory
import java.util.*
import java.util.concurrent.ConcurrentHashMap


class SchedulerStrategyImplementation(
    private val configuration: RateLimiterConfig,
    private val clazz: Class<out Job>
): SchedulerStrategy {
    private var scheduler: Scheduler = StdSchedulerFactory.getDefaultScheduler()

    override fun invoke(tokens: ConcurrentHashMap<UUID, Int>) {
        val job = JobBuilder
            .newJob(clazz)
            .withIdentity(configuration.messageType.name)
            .build()
        val trigger: Trigger = TriggerBuilder
            .newTrigger()
            .withIdentity(configuration.messageType.name)
            .withSchedule(CronScheduleBuilder.cronSchedule(configuration.schedule))
            .build()
        job.jobDataMap["tokens"] = tokens
        job.jobDataMap["configuration"] = configuration
        scheduler.scheduleJob(job, trigger);
        scheduler.start()
    }
}