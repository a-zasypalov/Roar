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
import com.gaoyun.roar.ui.Spacer

@Composable
internal fun UserScreenBackupBlock(
    onCreateBackupClick: (UserScreenContract.Event.OnCreateBackupClick) -> Unit,
    onUseBackup: (UserScreenContract.Event.OnUseBackup) -> Unit,
) {
//    val importBackupLauncher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
//        it.data?.data?.let { uri ->
//            context.contentResolver.openFileDescriptor(uri, "r")?.use { descriptor ->
//                if (descriptor.statSize <= Int.MAX_VALUE) {
//                    val data = ByteArray(descriptor.statSize.toInt())
//                    FileInputStream(descriptor.fileDescriptor).use { fileStream ->
//                        fileStream.read(data)
//
//                        AlertDialog.Builder(context)
//                            .setTitle(context.getString(R.string.replace_current_data_title))
//                            .setMessage(context.getString(R.string.replace_current_data_description))
//                            .setPositiveButton(context.getString(R.string.leave)) { dialog, _ ->
//                                dialog.dismiss()
//                                onUseBackup(
//                                    UserScreenContract.Event.OnUseBackup(
//                                        backup = data,
//                                        removeOld = false
//                                    )
//                                )
//                            }
//                            .setNegativeButton(context.getString(R.string.replace)) { dialog, _ ->
//                                dialog.dismiss()
//                                onUseBackup(
//                                    UserScreenContract.Event.OnUseBackup(
//                                        backup = data,
//                                        removeOld = true
//                                    )
//                                )
//                            }
//                            .create()
//                            .show()
//                    }
//                }
//            }
//        }
//    }

    Row(modifier = Modifier.fillMaxWidth()) {
        FilledTonalButton(
            onClick = { onCreateBackupClick(UserScreenContract.Event.OnCreateBackupClick) },
            modifier = Modifier
                .weight(1f)
                .padding(end = 4.dp)
        ) {
            Icon(Icons.Filled.Save, contentDescription = null)
            Spacer(size = 6.dp)
            Text(
                text = "Export", //stringResource(id = R.string.export_button),
                style = MaterialTheme.typography.titleMedium
            )
        }

        FilledTonalButton(
            onClick = {
//                val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
//                    addCategory(Intent.CATEGORY_OPENABLE)
//                    type = "application/json"
//                }
//                importBackupLauncher.launch(intent)
            },
            modifier = Modifier
                .weight(1f)
                .padding(start = 4.dp)
        ) {
            Icon(Icons.Filled.Download, contentDescription = null)
            Spacer(size = 6.dp)
            Text(
                text = "Import", //stringResource(id = R.string.import_button),
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}