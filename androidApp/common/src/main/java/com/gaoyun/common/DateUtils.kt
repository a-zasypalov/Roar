package com.gaoyun.common

import java.time.format.DateTimeFormatter

object DateUtils {
    val ddMmYyyyWithDotsDateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
    val ddMmmYyyyDateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy")
    val ddMmmmYyyyDateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy")

    val ddMmmDateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd MMM")
    val ddMmmmDateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd MMMM")

    val hhMmTimeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm")
}
