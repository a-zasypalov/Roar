package com.gaoyun.roar.network

import io.ktor.client.*
import io.ktor.client.plugins.ResponseException
import io.ktor.http.*

internal suspend fun <T> HttpClient.requestAndCatch(
    block: suspend HttpClient.() -> T,
    error: suspend Throwable.() -> T,
): T = runCatching { block() }.getOrElse { error(it) }

internal suspend fun <T> HttpClient.requestAndCatch(
    block: suspend HttpClient.() -> T,
): T = requestAndCatch(
    {
        block()
    },
    {
        printStackTrace()
        handleDefaultApiErrors()
    }
)

internal fun Throwable.handleDefaultApiErrors(): Nothing =
    if (this is ResponseException) {
        when (this.response.status) {
            HttpStatusCode.BadRequest -> throw this
            HttpStatusCode.Unauthorized -> throw this
            HttpStatusCode.UpgradeRequired -> throw this
            else -> throw this
        }
    } else {
        throw this
    }
