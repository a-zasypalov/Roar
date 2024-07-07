package com.gaoyun.roar.ui.common.dialog

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun RemovePetConfirmationDialog(
    petName: String,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
//            Text(stringResource(id = R.string.are_you_sure))
            Text("Are you sure?")
        },
        text = {
//            Text(stringResource(id = R.string.delete_pet_confirmation, petName))
            Text("Do you want to delete $petName")
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
//                Text(stringResource(id = R.string.yes))
                Text("Yes")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
//                Text(stringResource(id = R.string.cancel))
                Text("Cancel")
            }
        }
    )
}