package com.gaoyun.roar.util

interface BackupExportExecutor {
    fun registerExecutor()
    fun exportBackup(backup: String, callback: () -> Unit)
}

class NoopBackupExportExecutor: BackupExportExecutor {
    override fun exportBackup(backup: String, callback: () -> Unit) {}
    override fun registerExecutor() {}
}