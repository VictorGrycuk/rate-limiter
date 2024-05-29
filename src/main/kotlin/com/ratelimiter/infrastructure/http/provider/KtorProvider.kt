package com.ratelimiter.infrastructure.http.provider

import cc.rbbl.ktor_health_check.Health
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.ratelimiter.action.SendNotificationAction
import com.ratelimiter.infrastructure.http.Path
import com.ratelimiter.infrastructure.http.exception.RateLimitedException
import com.ratelimiter.infrastructure.http.handler.NotificationDTO
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.request.*
import io.ktor.server.routing.*

object KtorProvider {
    fun start() {
        val server = embeddedServer(Netty, port = 8080) {
            install(ContentNegotiation) {
                jackson()
            }
            install(Health)
            routing {
                storeFeedback (Actions.sendNotification)
            }
        }
        server.start(wait = true)
    }

    private fun Route.storeFeedback(sendNotification: SendNotificationAction) {
        post(Path.SEND_NOTIFICATION) {
            try {
                val mapper = jacksonObjectMapper()
                val notification: NotificationDTO = mapper.readValue(call.receiveText())
                sendNotification(notification.toNotification())
                call.response.status(HttpStatusCode.OK)
            } catch (e: RateLimitedException) {
                call.response.status(HttpStatusCode.TooManyRequests)
            } catch (e: Exception) {
                println(e.message)
            }
        }
    }
}