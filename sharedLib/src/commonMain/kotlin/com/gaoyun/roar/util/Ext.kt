@file:OptIn(ExperimentalTime::class)

package com.gaoyun.roar.util

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

fun Instant.toLocalDate() = this.toLocalDateTime(TimeZone.currentSystemDefault()).date

//Used on iOS
fun LocalDateTime.secondsFromNow() = this.toInstant(TimeZone.currentSystemDefault()).epochSeconds - Clock.System.now().epochSeconds