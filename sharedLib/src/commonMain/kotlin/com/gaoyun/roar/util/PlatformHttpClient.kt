package com.gaoyun.roar.util

import io.ktor.client.*

expect object PlatformHttpClient {
    fun httpClient(): HttpClient
}