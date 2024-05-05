package com.gaoyun.roar.ui.common.dialog

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource
import roar.sharedlib.generated.resources.Res
import roar.sharedlib.generated.resources.ok

object TimePicker {
    @Composable
    @OptIn(ExperimentalMaterial3Api::class, ExperimentalResourceApi::class)
    fun pickTime(
        onTimePicked: (Int, Int) -> Unit,
        onDismiss: () -> Unit,
        hourAndMinutes: List<Int>? = null,
    ) {
        val state = rememberTimePickerState(
            initialHour = hourAndMinutes?.getOrNull(0) ?: 12,
            initialMinute = hourAndMinutes?.getOrNull(1) ?: 0
        )
        DatePickerDialog(
            confirmButton = {
                Button(onClick = {
                    onTimePicked(state.hour, state.minute)
                }) {
                    Text(stringResource(Res.string.ok))
                }
            },
            onDismissRequest = onDismiss,
        ) {
            TimePicker(state = state, modifier = Modifier.align(Alignment.CenterHorizontally).padding(top = 16.dp))
        }
    }
}