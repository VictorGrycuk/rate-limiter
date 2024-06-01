package com.ratelimiter.infrastructure.services.ratelimiter.strategy

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.*
import org.quartz.JobDataMap
import org.quartz.JobDetail
import org.quartz.JobExecutionContext
import java.util.*
import java.util.concurrent.ConcurrentHashMap

class FixedWindowRefillStrategyTest {
    private val jobContext: JobExecutionContext = mock()
    private val jobDetail: JobDetail = mock()
    private val jobDataMap: JobDataMap = mock()
    private val tokensMap: ConcurrentHashMap<UUID, String> = mock()
    private val fixedWindowRefillStrategy = FixedWindowRefillStrategy()

    @BeforeEach
    fun setUp() {
        whenever(jobContext.jobDetail).thenReturn(jobDetail)
        whenever(jobDetail.jobDataMap).thenReturn(jobDataMap)
        whenever(jobDataMap[any()]).thenReturn(tokensMap)
    }

    @Test
    fun `should clear user tokens map when executed`() {
        fixedWindowRefillStrategy.execute(jobContext)

        verify(tokensMap, times(1)).clear()
    }
}