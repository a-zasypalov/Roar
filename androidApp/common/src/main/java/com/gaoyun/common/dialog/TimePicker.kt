package com.gaoyun.common.dialog

import android.text.format.DateFormat
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat

object TimePicker {
    fun pickTime(
        title: String,
        activity: AppCompatActivity,
        onTimePicked: (Int, Int) -> Unit,
        hourAndMinutes: List<Int>? = null,
    ) {
        val timeFormat = if (DateFormat.is24HourFormat(activity)) TimeFormat.CLOCK_24H else TimeFormat.CLOCK_12H

        val dialogBuilder = MaterialTimePicker.Builder()
            .setTitleText(title)
            .setTimeFormat(timeFormat)
            .setInputMode(MaterialTimePicker.INPUT_MODE_CLOCK)

        if (hourAndMinutes != null) {
            dialogBuilder.setHour(hourAndMinutes[0])
            dialogBuilder.setMinute(hourAndMinutes[1])
        }

        dialogBuilder.build().apply {
            addOnPositiveButtonClickListener {
                onTimePicked(hour, minute)
            }
            show(activity.supportFragmentManager, "TimePicker")
        }
    }
}