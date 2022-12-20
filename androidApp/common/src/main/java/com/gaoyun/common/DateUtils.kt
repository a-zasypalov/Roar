package com.gaoyun.common

import com.gaoyun.roar.util.toLocalDate
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.minus
import java.time.format.DateTimeFormatter

object DateUtils {

    val ddMmmYyyyDateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy")

    val ddMmmDateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd MMM")
    val ddMmmmDateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd MMMM")

    val ddMmYyyyWithDotsDateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")

    val hhMmTimeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm")

    fun LocalDate.yearsFromNow() = (Clock.System.now().toLocalDate() - this).years
    fun LocalDate.monthsFromNow() = (Clock.System.now().toLocalDate() - this).months % 12
    fun LocalDate.daysFromNow() = (Clock.System.now().toLocalDate() - this).days % 365

}

