package com.gaoyun.roar.util

import kotlinx.datetime.Instant
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

actual fun Instant.formatDate(pattern: String, defValue: String): String {
    return try {
        SimpleDateFormat(pattern, Locale.getDefault()).format(Date(this.toEpochMilliseconds()))
    } catch (e: Exception) {
        defValue
    }
}

actual fun String.parseDate(pattern: String, defValue: Long): Long {
    return try {
        SimpleDateFormat(pattern, Locale.getDefault()).parse(this)?.time ?: defValue
    } catch (e: Exception) {
        defValue
    }
}