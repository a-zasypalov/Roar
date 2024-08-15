package com.gaoyun.roar.domain

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.gaoyun.roar.network.SynchronisationApi
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

private const val TAG = "Nightly sync"

class NightlyAlarmReceiver : BroadcastReceiver(), KoinComponent {
    private val workManager: WorkManager by inject()

    companion object {
        private const val NIGHTLY_SYNC_WORK_NAME = "nightly_sync"
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        val request = OneTimeWorkRequestBuilder<NightlySyncWorker>().build()
        workManager.enqueueUniqueWork(NIGHTLY_SYNC_WORK_NAME, ExistingWorkPolicy.REPLACE, request)
    }
}


class NightlySyncWorker(
    context: Context,
    params: WorkerParameters,
    private val api: SynchronisationApi,
    private val synchronisationScheduler: SynchronisationScheduler
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        api.retrieveBackup(onFinish = { Log.d(TAG, "Nightly sync succeed: $it") })
        synchronisationScheduler.scheduleNightlySynchronisation()
        return Result.success()
    }
}