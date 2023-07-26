package com.gaoyun.roar.android

import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.android.play.core.ktx.isFlexibleUpdateAllowed
import com.google.android.play.core.ktx.isImmediateUpdateAllowed

class AppUpdater(private val context: AppCompatActivity) {

    private val appUpdateManager by lazy { AppUpdateManagerFactory.create(context) }
    private val activityResultLauncher = context.registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) {}

    fun checkAppUpdate() {
        appUpdateManager.appUpdateInfo.addOnSuccessListener { appUpdateInfo ->
            val availability = appUpdateInfo.updateAvailability()
            if (availability == UpdateAvailability.UPDATE_AVAILABLE) {
                when {
                    appUpdateInfo.isImmediateUpdateAllowed -> startUpdate(appUpdateInfo, AppUpdateType.IMMEDIATE)
                    appUpdateInfo.isFlexibleUpdateAllowed -> startUpdate(appUpdateInfo, AppUpdateType.FLEXIBLE)
                }
            }
        }
    }

    private fun startUpdate(appUpdateInfo: AppUpdateInfo, updateType: Int) {
        appUpdateManager.startUpdateFlowForResult(appUpdateInfo, activityResultLauncher, AppUpdateOptions.newBuilder(updateType).build())
    }

}