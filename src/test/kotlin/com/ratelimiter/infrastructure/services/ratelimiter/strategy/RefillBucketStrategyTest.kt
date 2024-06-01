package com.ratelimiter.infrastructure.services.ratelimiter.strategy

import com.ratelimiter.mothers.getRandomConfiguration
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.*
import org.quartz.JobDataMap
import org.quartz.JobDetail
import org.quartz.JobExecutionContext
import java.util.*
import java.util.concurrent.ConcurrentHashMap

class RefillBucketStrategyTest {
    private val jobContext: JobExecutionContext = mock()
    private val jobDetail: JobDetail = mock()
    private val jobDataMap: JobDataMap = mock()
//    private val tokensMap: ConcurrentHashMap<UUID, Int> = mock()
    private val refillBucketStrategy = RefillBucketStrategy()

    @BeforeEach
    fun setUp() {
        whenever(jobContext.jobDetail).thenReturn(jobDetail)
        whenever(jobDetail.jobDataMap).thenReturn(jobDataMap)
    }

    @Test
    fun `should refill tokens when they are below the max`() {
        val maxTokens = 1
        val config = getRandomConfiguration(maxTokens = maxTokens)
        val tokensMap = ConcurrentHashMap<UUID, Int>()
        val userId = UUID.randomUUID()

        tokensMap[userId] = 0
        whenever(jobDataMap["tokens"]).thenReturn(tokensMap)
        whenever(jobDataMap["configuration"]).thenReturn(config)

        refillBucketStrategy.execute(jobContext)

        assertThat(tokensMap[userId]).isEqualTo(1)
    }

    @Test
    fun `should not refill tokens when they are at max`() {
        val maxTokens = 2
        val config = getRandomConfiguration(maxTokens = maxTokens)
        val tokensMap = ConcurrentHashMap<UUID, Int>()
        val userId = UUID.randomUUID()

        tokensMap[userId] = 2
        whenever(jobDataMap["tokens"]).thenReturn(tokensMap)
        whenever(jobDataMap["configuration"]).thenReturn(config)

        refillBucketStrategy.execute(jobContext)

        assertThat(tokensMap[userId]).isEqualTo(2)
    }
}