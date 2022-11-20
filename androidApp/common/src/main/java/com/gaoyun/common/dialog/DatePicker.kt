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
        val dialogBuilder = MaterialDatePicker.Builder
            .datePicker()
            .setTitleText(title)
            .setCalendarConstraints(
                CalendarConstraints
                    .Builder()
                    .setStart(start)
                    .setEnd(end)
                    .build()
            )

        if (selectedDateMillis != null) {
            dialogBuilder.setSelection(selectedDateMillis)
        } else {
            dialogBuilder.setSelection(System.currentTimeMillis())
        }

        val dialog = dialogBuilder.build()
        dialog.addOnPositiveButtonClickListener(onDatePicked)
        dialog.show(fragmentManager, "DatePicker")
    }

}