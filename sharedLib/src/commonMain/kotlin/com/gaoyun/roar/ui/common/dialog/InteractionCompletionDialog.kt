package com.gaoyun.roar.ui.common.dialog

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import com.gaoyun.roar.util.DateFormats
import com.gaoyun.roar.util.SharedDateUtils
import com.gaoyun.roar.util.formatDateTime
import com.gaoyun.roar.util.toLocalDate
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource
import roar.sharedlib.generated.resources.Res
import roar.sharedlib.generated.resources.interaction_completion_dialog_description
import roar.sharedlib.generated.resources.interaction_completion_dialog_title
import roar.sharedlib.generated.resources.on_date_arg
import roar.sharedlib.generated.resources.today

@OptIn(ExperimentalResourceApi::class)
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

    val date = if (dateTime.date.year == SharedDateUtils.currentYear()) {
        dateTime.formatDateTime(DateFormats.ddMmmDateFormat)
    } else {
        dateTime.formatDateTime(DateFormats.ddMmmYyyyDateFormat)
    }

    AlertDialog(
        onDismissRequest = { showCompleteReminderDateDialog.value = false },
        title = {
            Text(stringResource(resource = Res.string.interaction_completion_dialog_title))
        },
        text = {
            Text(stringResource(resource = Res.string.interaction_completion_dialog_description, date))
        },
        confirmButton = {
            TextButton(onClick = onConfirmButtonClick) {
                Text(stringResource(resource = Res.string.today))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissButtonClick) {
                Text(stringResource(resource = Res.string.on_date_arg, date))
            }
        }
    )
}