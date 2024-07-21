package com.gaoyun.roar.ui.common.dialog

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import org.jetbrains.compose.resources.stringResource
import roar.sharedlib.generated.resources.Res
import roar.sharedlib.generated.resources.are_you_sure
import roar.sharedlib.generated.resources.cancel
import roar.sharedlib.generated.resources.delete_pet_confirmation
import roar.sharedlib.generated.resources.yes

@Composable
fun RemovePetConfirmationDialog(
    petName: String,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(stringResource(resource = Res.string.are_you_sure))
        },
        text = {
            Text(stringResource(resource = Res.string.delete_pet_confirmation, petName))
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(stringResource(resource = Res.string.yes))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(resource = Res.string.cancel))
            }
        }
    )
}