package com.gaoyun.roar.util

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atTime
import kotlinx.datetime.minus
import kotlinx.datetime.toLocalDateTime

object SharedDateUtils {
    val MAX_DATE = LocalDateTime(LocalDate(2970, Month.DECEMBER, 31), LocalTime(0, 0, 0))
    val MIN_DATE = LocalDateTime(LocalDate(1970, Month.JANUARY, 1), LocalTime(0, 0, 0))

    fun currentDateAt(hour: Int, minute: Int) = Clock.System.now().toLocalDate().atTime(hour = hour, minute = minute)
    fun currentDate() = Clock.System.now().toLocalDate()
    fun currentDateTime() = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())

    fun currentYear() = currentDate().year

    fun LocalDate.yearsFromNow() = (Clock.System.now().toLocalDate() - this).years
    fun LocalDate.monthsFromNow() = (Clock.System.now().toLocalDate() - this).months % 12
    fun LocalDate.daysFromNow() = (Clock.System.now().toLocalDate() - this).days % 365
}