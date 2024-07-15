package com.gaoyun.roar.util

import android.app.Activity
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.format
import org.koin.core.component.KoinComponent

class BackupExportExecutorImpl(private val activityProvider: ActivityProvider) : KoinComponent, BackupExportExecutor {
    private lateinit var exportBackupLauncher: ActivityResultLauncher<Intent>
    private val coroutineScope = CoroutineScope(Dispatchers.Main)
    private var backupState: String = ""
    private var callback: () -> Unit = {}

    override fun registerExecutor() {
        (activityProvider.activeActivity as? AppCompatActivity)?.let { activity ->
            exportBackupLauncher = activity.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                coroutineScope.launch {
                    if (result.resultCode == Activity.RESULT_OK) {
                        result.data?.data?.let { uri ->
                            activity.contentResolver.openOutputStream(uri)?.use { stream ->
                                stream.write(backupState.toByteArray())
                                callback()
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
        this.callback = callback
        exportBackupLauncher.launch(intent)
    }
}