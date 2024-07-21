package com.gaoyun.roar.ui.common.dialog

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.gaoyun.roar.util.toLocalDate
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import org.jetbrains.compose.resources.stringResource
import roar.sharedlib.generated.resources.Res
import roar.sharedlib.generated.resources.ok
import kotlin.time.Duration.Companion.days

object DatePicker {
    @Composable
    @OptIn(ExperimentalMaterial3Api::class)
    fun pickDate(
        title: String,
        onDatePicked: (Long) -> Unit,
        onDismiss: () -> Unit,
        selectedDateMillis: Long? = null,
        start: Instant = Clock.System.now().minus((30 * 365).days),
        end: Instant = Clock.System.now().plus((30 * 365).days),
    ) {
        val state = rememberDatePickerState(
            yearRange = start.toLocalDate().year..end.toLocalDate().year,
            initialDisplayedMonthMillis = selectedDateMillis ?: Clock.System.now().toEpochMilliseconds(),
            initialSelectedDateMillis = selectedDateMillis ?: Clock.System.now().toEpochMilliseconds(),
        )
        DatePickerDialog(
            confirmButton = {
                Button(onClick = {
                    state.selectedDateMillis?.let { onDatePicked(it) }
                }) {
                    Text(stringResource(Res.string.ok))
                }
            },
            onDismissRequest = onDismiss,
        ) {
            DatePicker(
                state = state,
                title = { Text(title, modifier = Modifier.padding(start = 24.dp, top = 16.dp)) }
            )
        }
    }
}