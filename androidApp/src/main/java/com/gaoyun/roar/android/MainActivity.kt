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
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.gaoyun.common.navigation.NavigationAction
import com.gaoyun.common.navigation.NavigationKeys
import com.gaoyun.common.theme.RoarTheme
import com.gaoyun.feature_add_pet.AddPetAvatarDestination
import com.gaoyun.feature_add_pet.AddPetPetTypeDestination
import com.gaoyun.feature_add_pet.AddPetSetupDestination
import com.gaoyun.feature_add_pet.pet_data.AddPetDataDestination
import com.gaoyun.feature_create_reminder.AddReminderCompleteDestination
import com.gaoyun.feature_create_reminder.AddReminderDestination
import com.gaoyun.feature_create_reminder.setup.SetupReminderDestination
import com.gaoyun.feature_home_screen.HomeScreenDestination
import com.gaoyun.feature_interactions.InteractionScreenDestination
import com.gaoyun.feature_onboarding.OnboardingRootScreen
import com.gaoyun.feature_pet_screen.PetScreenDestination
import com.gaoyun.feature_user_registration.UserRegistrationDestination
import com.gaoyun.feature_user_screen.about_screen.AboutScreenDestination
import com.gaoyun.feature_user_screen.edit_user.EditUserScreenDestination
import com.gaoyun.feature_user_screen.user_screen.UserScreenDestination
import com.gaoyun.roar.presentation.LAUNCH_LISTEN_FOR_EFFECTS
import com.gaoyun.roar.util.ColorTheme
import com.gaoyun.roar.util.PreferencesKeys
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by inject()

    private val isOnboardingComplete by lazy {
        this.getSharedPreferences("app_prefs", MODE_PRIVATE)
            .getBoolean(PreferencesKeys.ONBOARDING_COMPLETE, false)
    }

    private val isDynamicColorsActive by lazy {
        this.getSharedPreferences("app_prefs", MODE_PRIVATE)
            .getBoolean(PreferencesKeys.DYNAMIC_COLORS_ACTIVE, true)
    }

    private val colorTheme by lazy {
        this.getSharedPreferences("app_prefs", MODE_PRIVATE)
            .getString(PreferencesKeys.COLOR_THEME, null)?.let { ColorTheme.valueOf(it) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )

        setContent {
            RoarTheme(
                userPreferenceDynamicColorsIsActive = isDynamicColorsActive,
                colorTheme = colorTheme ?: ColorTheme.Orange
            ) {
                Surface(tonalElevation = RoarTheme.BACKGROUND_SURFACE_ELEVATION) {
                    GlobalDestinationState(isOnboardingComplete = isOnboardingComplete)
                }
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
            .setTitle(com.gaoyun.common.R.string.alert)
            .setMessage(com.gaoyun.common.R.string.notification_permission_dialog_text)
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

    @Composable
    fun GlobalDestinationState(isOnboardingComplete: Boolean) {
        val navController = rememberNavController()

        LaunchedEffect(LAUNCH_LISTEN_FOR_EFFECTS) {
            viewModel.navigationEffect.onEach { destination ->
                when (destination) {
                    is NavigationAction.NavigateTo -> navController.navigate(destination.path)
                    is NavigationAction.NavigateBack -> navController.navigateUp()
                    is NavigationAction.PopTo -> navController.popBackStack(destination.path, inclusive = destination.inclusive)
                }
            }.collect()
        }

        NavHost(
            navController = navController,
            startDestination = if (isOnboardingComplete) NavigationKeys.Route.HOME_ROUTE else NavigationKeys.Route.ONBOARDING_ROUTE,
        ) {
            composable(NavigationKeys.Route.ONBOARDING_ROUTE) {
                OnboardingRootScreen(navHostController = navController)
            }
            composable(NavigationKeys.Route.HOME_ROUTE) {
                HomeScreenDestination(onNavigationCall = viewModel::navigate)
            }

            composable(NavigationKeys.Route.REGISTER_USER_ROUTE) {
                UserRegistrationDestination(onNavigationCall = viewModel::navigate)
            }

            composable(NavigationKeys.Route.ADD_PET_ROUTE) {
                AddPetPetTypeDestination(onNavigationCall = viewModel::navigate)
            }

            composable(
                route = NavigationKeys.Route.ADD_PET_AVATAR_ROUTE,
                arguments = listOf(navArgument(NavigationKeys.Arg.PET_TYPE_KEY) { type = NavType.StringType })
            ) {
                AddPetAvatarDestination(
                    petType = it.arguments?.getString(NavigationKeys.Arg.PET_TYPE_KEY) ?: "",
                    onNavigationCall = viewModel::navigate
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
                    petType = it.arguments?.getString(NavigationKeys.Arg.PET_TYPE_KEY) ?: "",
                    petId = it.arguments?.getString(NavigationKeys.Arg.PET_ID_KEY),
                    onNavigationCall = viewModel::navigate
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
                    onNavigationCall = viewModel::navigate,
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
                    onNavigationCall = viewModel::navigate,
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
                    onNavigationCall = viewModel::navigate,
                    petId = it.arguments?.getString(NavigationKeys.Arg.PET_ID_KEY) ?: ""
                )
            }

            composable(
                route = NavigationKeys.Route.PET_DETAIL_ROUTE,
                arguments = listOf(navArgument(NavigationKeys.Arg.PET_ID_KEY) { type = NavType.StringType })
            ) {
                PetScreenDestination(viewModel::navigate, petId = it.arguments?.getString(NavigationKeys.Arg.PET_ID_KEY) ?: "")
            }

            composable(
                route = NavigationKeys.Route.ADD_REMINDER_ROUTE,
                arguments = listOf(navArgument(NavigationKeys.Arg.PET_ID_KEY) { type = NavType.StringType })
            ) {
                AddReminderDestination(
                    onNavigationCall = viewModel::navigate,
                    petId = it.arguments?.getString(NavigationKeys.Arg.PET_ID_KEY) ?: ""
                )
            }

            composable(
                route = NavigationKeys.Route.SETUP_REMINDER_ROUTE,
                arguments = listOf(
                    navArgument(NavigationKeys.Arg.PET_ID_KEY) { type = NavType.StringType },
                    navArgument(NavigationKeys.Arg.TEMPLATE_ID_KEY) { type = NavType.StringType }
                )
            ) {
                SetupReminderDestination(
                    onNavigationCall = viewModel::navigate,
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
                    onNavigationCall = viewModel::navigate,
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
                    onNavigationCall = viewModel::navigate,
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
                    onNavigationCall = viewModel::navigate,
                    interactionId = it.arguments?.getString(NavigationKeys.Arg.INTERACTION_ID_KEY) ?: "",
                )
            }

            composable(NavigationKeys.Route.USER_ROUTE) {
                UserScreenDestination(viewModel::navigate)
            }

            composable(NavigationKeys.Route.USER_EDIT_ROUTE) {
                EditUserScreenDestination(viewModel::navigate)
            }

            composable(NavigationKeys.Route.ABOUT_ROUTE) {
                AboutScreenDestination(viewModel::navigate)
            }
        }
    }

}