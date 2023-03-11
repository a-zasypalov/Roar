package com.gaoyun.common.dialog

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import com.gaoyun.common.DateUtils
import com.gaoyun.roar.util.toLocalDate
import kotlinx.datetime.Clock
import kotlinx.datetime.toKotlinLocalDateTime
import java.time.LocalDateTime

@Composable
fun InteractionCompletionDialog(
    showCompleteReminderDateDialog: MutableState<Boolean>,
    onConfirmButtonClick: () -> Unit,
    onDismissButtonClick: () -> Unit,
    dateTime: LocalDateTime
) {
    if (dateTime.toKotlinLocalDateTime().date == Clock.System.now().toLocalDate()) {
        onDismissButtonClick.invoke()
        return
    }
    val date = dateTime.format(DateUtils.ddMmmDateFormatter)
    AlertDialog(
        onDismissRequest = { showCompleteReminderDateDialog.value = false },
        title = { Text("When was it completed?") },
        text = { Text("Was the interaction completed today or according its date on $date?") },
        confirmButton = {
            TextButton(onClick = onConfirmButtonClick) {
                Text("Today")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissButtonClick) {
                Text("On $date")
            }
        }
    )
}