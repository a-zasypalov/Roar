package com.gaoyun.roar.util

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

fun Instant.toLocalDate() = this.toLocalDateTime(TimeZone.currentSystemDefault()).date

//Used on iOS
fun LocalDateTime.secondsFromNow() = this.toInstant(TimeZone.currentSystemDefault()).epochSeconds - Clock.System.now().epochSeconds