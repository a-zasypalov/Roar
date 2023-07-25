package com.gaoyun.roar.android

import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability

class AppUpdater(private val context: AppCompatActivity) {

    private val appUpdateManager by lazy { AppUpdateManagerFactory.create(context) }
    private val activityResultLauncher by lazy {
        context.registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result: ActivityResult ->
            println("UPDAAAAAAAAATE")
        }
    }

    fun checkAppUpdate() {
        appUpdateManager.appUpdateInfo.addOnSuccessListener { appUpdateInfo ->
            val availability = appUpdateInfo.updateAvailability()
            if (availability == UpdateAvailability.UPDATE_AVAILABLE) {
                if (appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                    startUpdate(appUpdateInfo, AppUpdateType.IMMEDIATE)
                } else if (appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)) {
                    val listener = InstallStateUpdatedListener { state ->
                        if (state.installStatus() == InstallStatus.DOWNLOADED) {
                            startUpdate(appUpdateInfo, AppUpdateType.FLEXIBLE)
                            Snackbar.make(
                                context.window.decorView.rootView,
                                "An update has just been downloaded.",
                                Snackbar.LENGTH_INDEFINITE
                            ).apply {
                                setAction("RESTART") { appUpdateManager.completeUpdate() }
                                show()
                            }
                        }
                    }

                    appUpdateManager.registerListener(listener)
                }
            }
        }
    }

    private fun startUpdate(appUpdateInfo: AppUpdateInfo, updateType: Int) {
        appUpdateManager.startUpdateFlowForResult(appUpdateInfo, activityResultLauncher, AppUpdateOptions.newBuilder(updateType).build())
    }

}