package com.gaoyun.roar.util

import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

fun Instant.toLocalDate() = this.toLocalDateTime(TimeZone.currentSystemDefault()).date