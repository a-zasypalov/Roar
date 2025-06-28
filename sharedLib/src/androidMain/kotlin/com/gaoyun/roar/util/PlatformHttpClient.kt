package com.gaoyun.roar.util

import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.engine.cio.endpoint
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.ContentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

actual object PlatformHttpClient {
    private const val TIMEOUT_SECONDS = 60L

    val AndroidHttpLogger = object : Logger {
        override fun log(message: String) {
            Log.d("KtorHttpClient", message)
        }
    }

    actual fun httpClient() = HttpClient(CIO) {
        engine {
            maxConnectionsCount = 1000
            endpoint {
                connectTimeout = TIMEOUT_SECONDS * 1000
                connectAttempts = 5
            }
        }
        install(Logging) {
            logger = AndroidHttpLogger
            level = LogLevel.BODY
        }
        install(ContentNegotiation) {
            json(
                contentType = ContentType.Any,
                json = Json {
                    ignoreUnknownKeys = true
                    prettyPrint = true
                    isLenient = true
                }
            )
        }
    }
}