package com.gaoyun.roar.util

import android.app.Activity
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.gaoyun.roar.R
import com.gaoyun.roar.presentation.user_screen.UserScreenContract
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.format
import org.koin.core.component.KoinComponent
import java.io.FileInputStream

class BackupHandlerImpl(private val activityProvider: ActivityProvider) : KoinComponent, BackupHandler {
    private var exportBackupLauncher: ActivityResultLauncher<Intent>? = null
    private var importBackupLauncher: ActivityResultLauncher<Intent>? = null
    private val coroutineScope = CoroutineScope(Dispatchers.Main)
    private var backupState: String = ""
    private var exportCallback: () -> Unit = {}
    private var onUseBackup: (UserScreenContract.Event.OnUseBackup) -> Unit = {}

    override fun unregisterExecutor() {
        exportBackupLauncher?.unregister()
        importBackupLauncher?.unregister()
        exportBackupLauncher = null
        importBackupLauncher = null
    }

    override fun registerExecutor() {
        (activityProvider.mainActivity as? AppCompatActivity)?.let { activity ->
            exportBackupLauncher = activity.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                coroutineScope.launch {
                    if (result.resultCode == Activity.RESULT_OK) {
                        result.data?.data?.let { uri ->
                            activity.contentResolver.openOutputStream(uri)?.use { stream ->
                                stream.write(backupState.toByteArray())
                                exportCallback()
                            }
                        }
                    }
                }
            }

            importBackupLauncher = activity.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                result.data?.data?.let { uri ->
                    activity.contentResolver.openFileDescriptor(uri, "r")?.use { descriptor ->
                        if (descriptor.statSize <= Int.MAX_VALUE) {
                            val data = ByteArray(descriptor.statSize.toInt())
                            FileInputStream(descriptor.fileDescriptor).use { fileStream ->
                                fileStream.read(data)

                                AlertDialog.Builder(activity)
                                    .setTitle(activity.getString(R.string.replace_current_data_title))
                                    .setMessage(activity.getString(R.string.replace_current_data_description))
                                    .setPositiveButton(activity.getString(R.string.leave)) { dialog, _ ->
                                        dialog.dismiss()
                                        onUseBackup(
                                            UserScreenContract.Event.OnUseBackup(
                                                backup = data,
                                                removeOld = false
                                            )
                                        )
                                    }
                                    .setNegativeButton(activity.getString(R.string.replace)) { dialog, _ ->
                                        dialog.dismiss()
                                        onUseBackup(
                                            UserScreenContract.Event.OnUseBackup(
                                                backup = data,
                                                removeOld = true
                                            )
                                        )
                                    }
                                    .create()
                                    .show()
                            }
                        }
                    }
                }
            }
        }
    }

    override fun exportBackup(backup: String, callback: () -> Unit) {
        val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "application/json"
            val currentDateTime = Clock.System.now().toLocalDate()
            val formattedDate = currentDateTime.format(LocalDate.Formats.ISO)
            putExtra(Intent.EXTRA_TITLE, "Roar_Backup_$formattedDate.json")
        }
        backupState = backup
        this.exportCallback = callback
        exportBackupLauncher?.launch(intent)
    }

    override fun importBackup(onUseBackup: (UserScreenContract.Event.OnUseBackup) -> Unit) {
        this.onUseBackup = onUseBackup

        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "application/json"
        }

        importBackupLauncher?.launch(intent)
    }
}