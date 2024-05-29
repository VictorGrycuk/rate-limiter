package com.ratelimiter.infrastructure.http

import com.ratelimiter.infrastructure.http.provider.KtorProvider

object Application {

    @JvmStatic
    fun main(args: Array<String>) {
        KtorProvider.start()
    }
}