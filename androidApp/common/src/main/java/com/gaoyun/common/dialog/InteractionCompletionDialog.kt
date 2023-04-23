package com.gaoyun.common.dialog

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.res.stringResource
import com.gaoyun.common.DateUtils
import com.gaoyun.common.R
import com.gaoyun.roar.util.toLocalDate
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toJavaLocalDateTime

@Composable
fun InteractionCompletionDialog(
    showCompleteReminderDateDialog: MutableState<Boolean>,
    onConfirmButtonClick: () -> Unit,
    onDismissButtonClick: () -> Unit,
    dateTime: LocalDateTime
) {
    if (dateTime.date == Clock.System.now().toLocalDate()) {
        onDismissButtonClick.invoke()
        return
    }

    val date = dateTime.toJavaLocalDateTime().format(DateUtils.ddMmmDateFormatter)
    AlertDialog(
        onDismissRequest = { showCompleteReminderDateDialog.value = false },
        title = { Text(stringResource(id = R.string.interaction_completion_dialog_title)) },
        text = { Text(stringResource(id = R.string.interaction_completion_dialog_description, date)) },
        confirmButton = {
            TextButton(onClick = onConfirmButtonClick) {
                Text(stringResource(id = R.string.today))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissButtonClick) {
                Text(stringResource(id = R.string.on_date_arg, date))
            }
        }
    )
}