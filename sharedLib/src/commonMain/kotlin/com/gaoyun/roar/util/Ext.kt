package com.gaoyun.roar.util

import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.math.abs
import kotlin.math.pow

fun Instant.toLocalDate() = this.toLocalDateTime(TimeZone.currentSystemDefault()).date

fun Double.formatDist(): String = when {
    this < 1000 -> this.toInt().toString() + " m"
    this < 10000 -> formatDec(this / 1000.0, 1) + " km"
    else -> (this / 1000f).toInt().toString() + " km"
}

fun formatDec(`val`: Double, dec: Int): String {
    val factor = 10.0.pow(dec.toDouble()).toInt()
    val front = `val`.toInt()
    val back = abs(`val` * factor).toInt() % factor
    return "$front.$back"
}