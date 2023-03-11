package com.gaoyun.common.dialog

import androidx.fragment.app.FragmentManager
import com.google.android.material.timepicker.MaterialTimePicker

object TimePicker {

    fun pickTime(
        title: String,
        fragmentManager: FragmentManager,
        onTimePicked: (Int, Int) -> Unit,
        hourAndMinutes: List<Int>? = null,
    ) {
        val dialogBuilder = MaterialTimePicker.Builder()
            .setTitleText(title)

        if (hourAndMinutes != null) {
            dialogBuilder.setHour(hourAndMinutes[0])
            dialogBuilder.setMinute(hourAndMinutes[1])
        }

        val dialog = dialogBuilder.build()
        dialog.addOnPositiveButtonClickListener {
            onTimePicked(dialog.hour, dialog.minute)
        }
        dialog.show(fragmentManager, "TimePicker")
    }

}