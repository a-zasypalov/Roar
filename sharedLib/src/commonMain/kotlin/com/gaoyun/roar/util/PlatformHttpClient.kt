package com.gaoyun.roar.util

import io.ktor.client.*

@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object PlatformHttpClient {
    fun httpClient(): HttpClient
}