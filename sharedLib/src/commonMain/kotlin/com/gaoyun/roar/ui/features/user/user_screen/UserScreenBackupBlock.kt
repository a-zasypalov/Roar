package com.gaoyun.roar.ui.features.user.user_screen

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.gaoyun.roar.presentation.user_screen.UserScreenContract
import com.gaoyun.roar.ui.common.composables.Spacer
import org.jetbrains.compose.resources.stringResource
import roar.sharedlib.generated.resources.Res
import roar.sharedlib.generated.resources.export_button
import roar.sharedlib.generated.resources.import_button

@Composable
internal fun UserScreenBackupBlock(
    backupClick: (UserScreenContract.Event) -> Unit,
) {

    Row(modifier = Modifier.fillMaxWidth()) {
        FilledTonalButton(
            onClick = { backupClick(UserScreenContract.Event.OnCreateBackupClick) },
            modifier = Modifier
                .weight(1f)
                .padding(end = 4.dp)
        ) {
            Icon(Icons.Filled.Save, contentDescription = null)
            Spacer(size = 6.dp)
            Text(
                text = stringResource(resource = Res.string.export_button),
                style = MaterialTheme.typography.titleMedium
            )
        }

        FilledTonalButton(
            onClick = { backupClick(UserScreenContract.Event.OnUseBackupClick) },
            modifier = Modifier
                .weight(1f)
                .padding(start = 4.dp)
        ) {
            Icon(Icons.Filled.Download, contentDescription = null)
            Spacer(size = 6.dp)
            Text(
                text = stringResource(resource = Res.string.import_button),
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}