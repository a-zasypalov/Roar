package com.gaoyun.common.dialog

import androidx.fragment.app.FragmentManager
import com.gaoyun.roar.util.DatetimeConstants
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import java.time.Instant

object DatePicker {
    fun pickDate(
        title: String,
        fragmentManager: FragmentManager,
        onDatePicked: (Long) -> Unit,
        selectedDateMillis: Long? = null,
        start: Long = Instant.now().minusMillis(30 * DatetimeConstants.YEAR_MILLIS).toEpochMilli(),
        end: Long = Instant.now().plusMillis(30 * DatetimeConstants.YEAR_MILLIS).toEpochMilli(),
    ) {
        MaterialDatePicker.Builder
            .datePicker()
            .setTitleText(title)
            .setCalendarConstraints(
                CalendarConstraints
                    .Builder()
                    .setStart(start)
                    .setEnd(end)
                    .build()
            )
            .setSelection(selectedDateMillis ?: System.currentTimeMillis())
            .build()
            .apply {
                addOnPositiveButtonClickListener(onDatePicked)
                show(fragmentManager, "DatePicker")
            }
    }
}