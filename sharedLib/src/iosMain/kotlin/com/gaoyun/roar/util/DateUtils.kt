package com.gaoyun.roar.util

import kotlinx.datetime.Instant
import kotlinx.datetime.toNSDate
import platform.Foundation.NSDateFormatter
import platform.Foundation.timeIntervalSince1970

actual fun Instant.formatDate(pattern: String, defValue: String): String {
    return try {
        val dateFormatter = NSDateFormatter()
        dateFormatter.dateFormat = pattern
        dateFormatter.stringFromDate(
            toNSDate()
        )
    } catch (e: Exception) {
        defValue
    }

}

actual fun String.parseDate(pattern: String, defValue: Long): Long {
    return try {
        val dateFormatter = NSDateFormatter()
        dateFormatter.dateFormat = pattern
        val result = dateFormatter.dateFromString(this)?.timeIntervalSince1970?.toLong()
        if (result != null) {
            result * 1000
        } else {
            defValue
        }
    } catch (e: Exception) {
        defValue
    }
}