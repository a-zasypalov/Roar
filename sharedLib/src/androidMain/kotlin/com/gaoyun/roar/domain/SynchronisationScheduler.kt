package com.gaoyun.roar.domain

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
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
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atTime
import kotlinx.datetime.plus
import kotlinx.datetime.toInstant
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.time.Duration
import java.util.concurrent.TimeUnit
import kotlin.time.toJavaDuration

class SynchronisationSchedulerImpl : SynchronisationScheduler, KoinComponent {

    private val workManager: WorkManager by inject()
    private val syncDelay = if (BuildConfig.DEBUG) 5L else 30L

    companion object {
        const val SYNC_WORK_NAME = "sync"
        const val NIGHTLY_SYNC_WORK_NAME = "nightly_sync"
    }

    override fun scheduleSynchronisation() {
        val request = OneTimeWorkRequestBuilder<SynchronisationWorker>()
            .setInitialDelay(syncDelay, TimeUnit.SECONDS)
            .build()

        workManager.enqueueUniqueWork(SYNC_WORK_NAME, ExistingWorkPolicy.REPLACE, request)
    }

    override fun scheduleNightlySynchronisation() {
        val delay = delayUntilNext3AM()
        println("Schedule nightly with delay: $delay")
        val request = PeriodicWorkRequestBuilder<NightlySyncWorker>(repeatInterval = Duration.ofDays(1))
            .setInitialDelay(delayUntilNext3AM())
            .build()
        workManager.enqueueUniquePeriodicWork(NIGHTLY_SYNC_WORK_NAME, ExistingPeriodicWorkPolicy.CANCEL_AND_REENQUEUE, request)
    }

    override fun stopNightlySynchronisation() {
        workManager.cancelUniqueWork(NIGHTLY_SYNC_WORK_NAME)
    }

    private fun delayUntilNext3AM(): Duration {
        val now = Clock.System.now()
        val next3AM = now.toLocalDate().plus(DatePeriod(days = 0)).atTime(hour = 3, minute = 0)
        val delayInSeconds = next3AM.toInstant(TimeZone.currentSystemDefault()).minus(now)
        return delayInSeconds.toJavaDuration()
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

class NightlySyncWorker(
    context: Context,
    params: WorkerParameters,
    private val api: SynchronisationApi,
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        api.retrieveBackup(onFinish = {
            //TODO: add nightly sync work
            println("Nightly sync succeed: $it")
        })
        return Result.success()
    }
}