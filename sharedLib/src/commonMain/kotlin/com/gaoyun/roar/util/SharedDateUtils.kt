package com.gaoyun.roar.util

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atTime
import kotlinx.datetime.toLocalDateTime

object SharedDateUtils {
    val MAX_DATE = LocalDateTime(LocalDate.fromEpochDays(Int.MAX_VALUE), LocalTime(0, 0, 0))
    val MIN_DATE = LocalDateTime(LocalDate.fromEpochDays(0), LocalTime(0, 0, 0))

    fun currentDateAt(hour: Int, minute: Int) = Clock.System.now().toLocalDate().atTime(hour = hour, minute = minute)
    fun currentDate() = Clock.System.now().toLocalDate()
    fun currentDateTime() = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
}