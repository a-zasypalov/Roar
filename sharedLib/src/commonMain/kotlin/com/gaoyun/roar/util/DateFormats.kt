package com.gaoyun.roar.util

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.toInstant

object DateFormats {
    const val DD_MMM_YYYY_DATE_FORMAT: String = "dd MMM yyyy"
    const val DD_MMMM_YYYY_DATE_FORMAT: String = "dd MMMM yyyy"

    const val DD_MMM_DATE_FORMAT: String = "dd MMM"
    const val DD_MMMM_DATE_FORMAT: String = "dd MMMM"

    const val HH_MM_TIME_FORMAT: String = "HH:mm"
}

fun LocalDate.formatDate(pattern: String, defValue: String = ""): String {
    return this.atStartOfDayIn(TimeZone.currentSystemDefault()).formatDate(pattern, defValue)
}

fun LocalDateTime.formatDateTime(pattern: String, defValue: String = ""): String {
    return this.toInstant(TimeZone.currentSystemDefault()).formatDate(pattern, defValue)
}

expect fun Instant.formatDate(pattern: String, defValue: String = ""): String

expect fun String.parseDate(pattern: String, defValue: Long = 0L): Long

fun Long.formatDate(pattern: String, defValue: String = ""): String {
    return Instant.fromEpochMilliseconds(this).formatDate(pattern, defValue)
}