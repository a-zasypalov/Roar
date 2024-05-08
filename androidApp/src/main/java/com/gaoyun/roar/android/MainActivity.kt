package com.gaoyun.roar.android

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.WindowManager
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import com.gaoyun.roar.ui.App
import com.gaoyun.roar.util.Platform
import com.gaoyun.roar.util.PreferencesKeys
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.gaoyun.common.R as CommonR

class MainActivity : AppCompatActivity() {

    private val isDynamicColorsActive by lazy {
        this.getSharedPreferences("app_prefs", MODE_PRIVATE)
            .getBoolean(PreferencesKeys.DYNAMIC_COLORS_ACTIVE, true)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )

        setContent {
            val dynamicColors = if (isDynamicColorsActive && Platform.supportsDynamicColor) {
                if (isSystemInDarkTheme()) dynamicDarkColorScheme(this) else dynamicLightColorScheme(this)
            } else null

            App(dynamicColors)
        }

        prepareNotificationChannel()

        lifecycleScope.launch {
            delay(500)
            if (Build.VERSION.SDK_INT >= 33) {
                notificationPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    private fun prepareNotificationChannel() {
        val id = "com.gaoyun.roar.RemindersChannel"
        val name = "Pet's Reminders"
        val des = "Channel for reminding about important things about your pet"

        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(id, name, importance)
        channel.description = des

        val manager = ContextCompat.getSystemService(this, NotificationManager::class.java)

        if (manager?.notificationChannels?.map { it.id }?.contains(id) != true) {
            manager?.createNotificationChannel(channel)
        }
    }

    private val notificationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (!isGranted) {
                if (Build.VERSION.SDK_INT >= 33) {
                    if (shouldShowRequestPermissionRationale(android.Manifest.permission.POST_NOTIFICATIONS)) {
                        showNotificationPermissionRationale()
                    } else {
                        showSettingDialog()
                    }
                }
            }
        }

    private fun showSettingDialog() {
        MaterialAlertDialogBuilder(this)
            .setTitle(CommonR.string.alert)
            .setMessage(CommonR.string.notification_permission_dialog_text)
            .setPositiveButton(com.gaoyun.common.R.string.ok) { _, _ ->
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                intent.data = Uri.parse("package:$packageName")
                startActivity(intent)
            }
            .setNegativeButton(com.gaoyun.common.R.string.cancel, null)
            .show()
    }

    private fun showNotificationPermissionRationale() {
        MaterialAlertDialogBuilder(this)
            .setTitle(com.gaoyun.common.R.string.alert)
            .setMessage(com.gaoyun.common.R.string.notification_permission_rationale_dialog_text)
            .setPositiveButton(com.gaoyun.common.R.string.ok) { _, _ ->
                if (Build.VERSION.SDK_INT >= 33) {
                    notificationPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
                }
            }
            .setNegativeButton(com.gaoyun.common.R.string.cancel, null)
            .show()
    }

    override fun onResume() {
        super.onResume()
        AppUpdater.checkAppUpdate(this)
    }
}