package com.gaoyun.roar.android

import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.gaoyun.roar.util.PreferencesKeys
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.android.play.core.ktx.isFlexibleUpdateAllowed
import com.google.android.play.core.ktx.isImmediateUpdateAllowed
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlin.time.Duration.Companion.days

object AppUpdater {

    fun checkAppUpdate(context: AppCompatActivity) {
        val appUpdateManager = AppUpdateManagerFactory.create(context)
        val lastCheckingDateTime = context.getSharedPreferences("app_prefs", AppCompatActivity.MODE_PRIVATE)
            .getLong(PreferencesKeys.LAST_UPDATE_CHECK_DATETIME, Long.MAX_VALUE)

        if (Instant.fromEpochMilliseconds(lastCheckingDateTime) + 1.days < Clock.System.now()) {
            appUpdateManager.appUpdateInfo.addOnSuccessListener { appUpdateInfo ->
                val availability = appUpdateInfo.updateAvailability()
                if (availability == UpdateAvailability.UPDATE_AVAILABLE) {
                    when {
                        appUpdateInfo.isImmediateUpdateAllowed -> startUpdate(context, appUpdateManager, appUpdateInfo, AppUpdateType.IMMEDIATE)
                        appUpdateInfo.isFlexibleUpdateAllowed -> startUpdate(context, appUpdateManager, appUpdateInfo, AppUpdateType.FLEXIBLE)
                    }
                }
            }
            context.getSharedPreferences("app_prefs", AppCompatActivity.MODE_PRIVATE).edit()
                .putLong(PreferencesKeys.LAST_UPDATE_CHECK_DATETIME, Clock.System.now().toEpochMilliseconds()).apply()
        }
    }

    private fun startUpdate(context: AppCompatActivity, appUpdateManager: AppUpdateManager, appUpdateInfo: AppUpdateInfo, updateType: Int) {
        val activityResultLauncher = context.registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) {}
        appUpdateManager.startUpdateFlowForResult(appUpdateInfo, activityResultLauncher, AppUpdateOptions.newBuilder(updateType).build())
    }

}