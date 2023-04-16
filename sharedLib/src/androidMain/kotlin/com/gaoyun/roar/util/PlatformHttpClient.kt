package com.gaoyun.roar.util

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.http.ContentType
import io.ktor.serialization.kotlinx.json.*

actual object PlatformHttpClient {
    private const val TIMEOUT_SECONDS = 60L

    actual fun httpClient() = HttpClient(CIO) {
        engine {
            maxConnectionsCount = 1000
            endpoint {
                connectTimeout = TIMEOUT_SECONDS * 1000
                connectAttempts = 5
            }
        }
        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.BODY
        }
        install(ContentNegotiation) {
            json(contentType = ContentType.Any)
        }
    }
}