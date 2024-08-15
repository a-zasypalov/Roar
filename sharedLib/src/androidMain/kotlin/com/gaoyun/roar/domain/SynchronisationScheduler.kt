package com.gaoyun.roar.domain

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.gaoyun.roar.BuildConfig
import com.gaoyun.roar.domain.backup.CreateBackupUseCase
import com.gaoyun.roar.network.SynchronisationApi
import com.gaoyun.roar.util.toLocalDate
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atTime
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.concurrent.TimeUnit
import kotlin.time.Duration.Companion.hours

private const val TAG = "SynchronisationScheduler"

class SynchronisationSchedulerImpl : SynchronisationScheduler, KoinComponent {

    private val workManager: WorkManager by inject()
    private val context: Context by inject()
    private val preferencesUseCase: AppPreferencesUseCase by inject()
    private val syncDelay = if (BuildConfig.DEBUG) 5L else 30L

    companion object {
        private const val SYNC_WORK_NAME = "sync"
        private const val NIGHTLY_SYNC_REQUEST_ID = 321001
    }

    override fun scheduleSynchronisation() {
        val request = OneTimeWorkRequestBuilder<SynchronisationWorker>()
            .setInitialDelay(syncDelay, TimeUnit.SECONDS)
            .build()

        workManager.enqueueUniqueWork(SYNC_WORK_NAME, ExistingWorkPolicy.REPLACE, request)
    }

    override fun scheduleNightlySynchronisation() {
        if (preferencesUseCase.nightlyScheduledAt() < Clock.System.now().toEpochMilliseconds()) {
            val scheduledTime = next3AMMillis()
            Log.d(TAG, "Schedule nightly at: ${Instant.fromEpochMilliseconds(scheduledTime).toLocalDateTime(TimeZone.currentSystemDefault())}")
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val alarmIntent = Intent(context, NightlyAlarmReceiver::class.java).let { intent ->
                PendingIntent.getBroadcast(context, NIGHTLY_SYNC_REQUEST_ID, intent, PendingIntent.FLAG_IMMUTABLE)
            }
            alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, scheduledTime, alarmIntent)
            preferencesUseCase.setNightlyScheduled(forTime = scheduledTime)
        }
    }

    private fun next3AMMillis(): Long {
        val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        val addHours = if (now.hour > 2) 24.hours else 0.hours
        val next3AM = Clock.System.now().plus(addHours).toLocalDate().atTime(hour = 3, minute = 0)
        return next3AM.toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds()
    }
}

class SynchronisationWorker(
    context: Context,
    params: WorkerParameters,
    private val createBackupUseCase: CreateBackupUseCase,
    private val api: SynchronisationApi,
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        return createBackupUseCase.createBackupToSync()
            .catch { Result.failure() }
            .map {
                try {
                    if (it != null) api.sendBackup(it)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                Result.success()
            }
            .first()
    }
}