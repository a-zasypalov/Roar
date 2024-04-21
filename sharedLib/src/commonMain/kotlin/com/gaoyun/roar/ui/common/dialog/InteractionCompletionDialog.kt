package com.gaoyun.roar.ui.common.dialog

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import com.gaoyun.roar.util.toLocalDate
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime

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

    //TODO: Format date
//    val date = if(dateTime.date.year == SharedDateUtils.currentYear()) {
//        dateTime.toJavaLocalDateTime().format(DateUtils.ddMmmDateFormatter)
//    } else {
//        dateTime.toJavaLocalDateTime().format(DateUtils.ddMmmYyyyDateFormatter)
//    }
    val date = ""

    AlertDialog(
        onDismissRequest = { showCompleteReminderDateDialog.value = false },
        title = {
//            Text(stringResource(id = R.string.interaction_completion_dialog_title))
            Text("When was it completed?")
        },
        text = {
//            Text(stringResource(id = R.string.interaction_completion_dialog_description, date))
            Text("Was the interaction completed today or according its date on $date?")
        },
        confirmButton = {
            TextButton(onClick = onConfirmButtonClick) {
//                Text(stringResource(id = R.string.today))
                Text("Today")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissButtonClick) {
//                Text(stringResource(id = R.string.on_date_arg, date))
                Text("On $date")
            }
        }
    )
}