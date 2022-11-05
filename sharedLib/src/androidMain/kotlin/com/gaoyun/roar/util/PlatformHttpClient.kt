package com.gaoyun.roar.util

import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.http.*
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

actual object PlatformHttpClient {
    private const val TIMEOUT_SECONDS = 60L

    actual fun httpClient() =
        HttpClient(OkHttp) {
            install(JsonFeature) {
                val json = kotlinx.serialization.json.Json { ignoreUnknownKeys = true }
                acceptContentTypes = acceptContentTypes + ContentType.Any
                serializer = KotlinxSerializer(json)
            }
            engine {
                val loggingInterceptor = HttpLoggingInterceptor()
                loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
                addInterceptor(loggingInterceptor)
                config {
                    callTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
                    connectTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
                    readTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
                    writeTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
                }
            }
        }
}