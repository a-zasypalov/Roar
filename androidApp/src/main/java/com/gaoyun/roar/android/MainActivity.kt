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
import androidx.compose.runtime.Composable
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.gaoyun.common.NavigationKeys
import com.gaoyun.common.theme.RoarTheme
import com.gaoyun.feature_add_pet.AddPetAvatarDestination
import com.gaoyun.feature_add_pet.AddPetDataDestination
import com.gaoyun.feature_add_pet.AddPetPetTypeDestination
import com.gaoyun.feature_add_pet.AddPetSetupDestination
import com.gaoyun.feature_create_reminder.AddReminderCompleteDestination
import com.gaoyun.feature_create_reminder.AddReminderDestination
import com.gaoyun.feature_create_reminder.setup.SetupReminderDestination
import com.gaoyun.feature_home_screen.HomeScreenDestination
import com.gaoyun.feature_interactions.InteractionScreenDestination
import com.gaoyun.feature_onboarding.OnboardingRootScreen
import com.gaoyun.feature_pet_screen.PetScreenDestination
import com.gaoyun.feature_user_registration.UserRegistrationDestination
import com.gaoyun.feature_user_screen.UserScreenDestination
import com.gaoyun.roar.util.PreferencesKeys
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val isOnboardingComplete by lazy {
        this.getSharedPreferences("app_prefs", MODE_PRIVATE)
            .getBoolean(PreferencesKeys.ONBOARDING_COMPLETE, false)
    }

    private val isDynamicColorsActive by lazy {
        this.getSharedPreferences("app_prefs", MODE_PRIVATE)
            .getBoolean(PreferencesKeys.DYNAMIC_COLORS_ACTIVE, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )

        setContent {
            RoarTheme(userPreferenceDynamicColorsIsActive = isDynamicColorsActive) {
                GlobalDestinationState(isOnboardingComplete = isOnboardingComplete)
            }
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

    private val notificationPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
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
            .setTitle("Notification Permission")
            .setMessage("Notification permission is required, Please allow notification permission from setting")
            .setPositiveButton("Ok") { _, _ ->
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                intent.data = Uri.parse("package:$packageName")
                startActivity(intent)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showNotificationPermissionRationale() {
        MaterialAlertDialogBuilder(this)
            .setTitle("Alert")
            .setMessage("Notification permission is required, to show notification")
            .setPositiveButton("Ok") { _, _ ->
                if (Build.VERSION.SDK_INT >= 33) {
                    notificationPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    @Composable
    fun GlobalDestinationState(isOnboardingComplete: Boolean) {
        val navController = rememberNavController()

        NavHost(
            navController = navController,
            startDestination = if (isOnboardingComplete) NavigationKeys.Route.HOME_ROUTE else NavigationKeys.Route.ONBOARDING_ROUTE,
        ) {
            composable(NavigationKeys.Route.ONBOARDING_ROUTE) {
                OnboardingRootScreen(navHostController = navController)
            }
            composable(NavigationKeys.Route.HOME_ROUTE) {
                HomeScreenDestination(
                    navHostController = navController
                )
            }

            composable(NavigationKeys.Route.REGISTER_USER_ROUTE) {
                UserRegistrationDestination(navHostController = navController)
            }

            composable(NavigationKeys.Route.ADD_PET_ROUTE) {
                AddPetPetTypeDestination(navHostController = navController)
            }

            composable(
                route = NavigationKeys.Route.ADD_PET_AVATAR_ROUTE,
                arguments = listOf(navArgument(NavigationKeys.Arg.PET_TYPE_KEY) { type = NavType.StringType })
            ) {
                AddPetAvatarDestination(
                    navHostController = navController,
                    petType = it.arguments?.getString(NavigationKeys.Arg.PET_TYPE_KEY) ?: ""
                )
            }

            composable(
                route = NavigationKeys.Route.PET_EDIT_AVATAR_ROUTE,
                arguments = listOf(
                    navArgument(NavigationKeys.Arg.PET_TYPE_KEY) { type = NavType.StringType },
                    navArgument(NavigationKeys.Arg.PET_ID_KEY) { type = NavType.StringType }
                )
            ) {
                AddPetAvatarDestination(
                    navHostController = navController,
                    petType = it.arguments?.getString(NavigationKeys.Arg.PET_TYPE_KEY) ?: "",
                    petId = it.arguments?.getString(NavigationKeys.Arg.PET_ID_KEY)
                )
            }

            composable(
                route = NavigationKeys.Route.ADD_PET_DATA_ROUTE,
                arguments = listOf(
                    navArgument(NavigationKeys.Arg.PET_TYPE_KEY) { type = NavType.StringType },
                    navArgument(NavigationKeys.Arg.AVATAR_KEY) { type = NavType.StringType },
                )
            ) {
                AddPetDataDestination(
                    navHostController = navController,
                    petType = it.arguments?.getString(NavigationKeys.Arg.PET_TYPE_KEY) ?: "",
                    avatar = it.arguments?.getString(NavigationKeys.Arg.AVATAR_KEY) ?: "",
                )
            }

            composable(
                route = NavigationKeys.Route.PET_EDIT_ROUTE,
                arguments = listOf(
                    navArgument(NavigationKeys.Arg.PET_ID_KEY) { type = NavType.StringType },
                    navArgument(NavigationKeys.Arg.PET_TYPE_KEY) { type = NavType.StringType },
                    navArgument(NavigationKeys.Arg.AVATAR_KEY) { type = NavType.StringType },
                )
            ) {
                AddPetDataDestination(
                    navHostController = navController,
                    petType = it.arguments?.getString(NavigationKeys.Arg.PET_TYPE_KEY) ?: "",
                    avatar = it.arguments?.getString(NavigationKeys.Arg.AVATAR_KEY) ?: "",
                    petId = it.arguments?.getString(NavigationKeys.Arg.PET_ID_KEY)
                )
            }

            composable(
                route = NavigationKeys.Route.ADD_PET_SETUP_ROUTE,
                arguments = listOf(navArgument(NavigationKeys.Arg.PET_ID_KEY) { type = NavType.StringType })
            ) {
                AddPetSetupDestination(
                    navHostController = navController,
                    petId = it.arguments?.getString(NavigationKeys.Arg.PET_ID_KEY) ?: ""
                )
            }

            composable(
                route = NavigationKeys.Route.PET_DETAIL_ROUTE,
                arguments = listOf(navArgument(NavigationKeys.Arg.PET_ID_KEY) { type = NavType.StringType })
            ) {
                PetScreenDestination(navController, petId = it.arguments?.getString(NavigationKeys.Arg.PET_ID_KEY) ?: "")
            }

            composable(
                route = NavigationKeys.Route.ADD_REMINDER_ROUTE,
                arguments = listOf(navArgument(NavigationKeys.Arg.PET_ID_KEY) { type = NavType.StringType })
            ) {
                AddReminderDestination(navController, petId = it.arguments?.getString(NavigationKeys.Arg.PET_ID_KEY) ?: "")
            }

            composable(
                route = NavigationKeys.Route.SETUP_REMINDER_ROUTE,
                arguments = listOf(
                    navArgument(NavigationKeys.Arg.PET_ID_KEY) { type = NavType.StringType },
                    navArgument(NavigationKeys.Arg.TEMPLATE_ID_KEY) { type = NavType.StringType }
                )
            ) {
                SetupReminderDestination(
                    navController,
                    petId = it.arguments?.getString(NavigationKeys.Arg.PET_ID_KEY) ?: "",
                    templateId = it.arguments?.getString(NavigationKeys.Arg.TEMPLATE_ID_KEY) ?: "custom"
                )
            }

            composable(
                route = NavigationKeys.Route.EDIT_REMINDER_ROUTE,
                arguments = listOf(
                    navArgument(NavigationKeys.Arg.PET_ID_KEY) { type = NavType.StringType },
                    navArgument(NavigationKeys.Arg.TEMPLATE_ID_KEY) { type = NavType.StringType },
                    navArgument(NavigationKeys.Arg.INTERACTION_ID_KEY) { type = NavType.StringType }
                )
            ) {
                SetupReminderDestination(
                    navController,
                    petId = it.arguments?.getString(NavigationKeys.Arg.PET_ID_KEY) ?: "",
                    templateId = it.arguments?.getString(NavigationKeys.Arg.TEMPLATE_ID_KEY) ?: "custom",
                    interactionId = it.arguments?.getString(NavigationKeys.Arg.INTERACTION_ID_KEY)
                )
            }

            composable(
                route = NavigationKeys.Route.SETUP_REMINDER_COMPLETE_ROUTE,
                arguments = listOf(
                    navArgument(NavigationKeys.Arg.PET_ID_KEY) { type = NavType.StringType },
                    navArgument(NavigationKeys.Arg.TEMPLATE_ID_KEY) { type = NavType.StringType },
                    navArgument(NavigationKeys.Arg.AVATAR_KEY) { type = NavType.StringType },
                )
            ) {
                AddReminderCompleteDestination(
                    navController,
                    petAvatar = it.arguments?.getString(NavigationKeys.Arg.AVATAR_KEY) ?: "",
                )
            }

            composable(
                route = NavigationKeys.Route.INTERACTION_DETAIL_ROUTE,
                arguments = listOf(
                    navArgument(NavigationKeys.Arg.INTERACTION_ID_KEY) { type = NavType.StringType },
                )
            ) {
                InteractionScreenDestination(
                    navController,
                    interactionId = it.arguments?.getString(NavigationKeys.Arg.INTERACTION_ID_KEY) ?: "",
                )
            }

            composable(
                route = NavigationKeys.Route.USER_ROUTE,
            ) {
                UserScreenDestination(navController)
            }
        }
    }

}