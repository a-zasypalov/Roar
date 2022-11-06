package com.gaoyun.roar.android

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
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
import com.gaoyun.feature_create_reminder.AddReminderDestination
import com.gaoyun.feature_home_screen.HomeScreenDestination
import com.gaoyun.feature_user_registration.UserRegistrationDestination
import com.google.accompanist.systemuicontroller.rememberSystemUiController

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            RoarTheme {
                Surface(color = MaterialTheme.colors.background) {
                    GlobalDestinationState()
                }
            }
        }

    }

    @Composable
    fun GlobalDestinationState() {
        val navController = rememberNavController()

        NavHost(
            navController = navController,
            startDestination = NavigationKeys.RouteGlobal.HOME_ROUTE
        ) {
            composable(NavigationKeys.RouteGlobal.HOME_ROUTE) {
                HomeScreenDestination(
                    navHostController = navController
                )
            }

            composable(NavigationKeys.RouteGlobal.REGISTER_USER_ROUTE) {
                UserRegistrationDestination(navHostController = navController)
            }

            composable(NavigationKeys.RouteGlobal.ADD_PET_ROUTE) {
                AddPetPetTypeDestination(navHostController = navController)
            }

            composable(
                route = NavigationKeys.RouteGlobal.ADD_PET_AVATAR_ROUTE,
                arguments = listOf(navArgument(NavigationKeys.Arg.PET_TYPE_KEY) { type = NavType.StringType })
            ) {
                AddPetAvatarDestination(
                    navHostController = navController,
                    petType = it.arguments?.getString(NavigationKeys.Arg.PET_TYPE_KEY) ?: ""
                )
            }

            composable(
                route = NavigationKeys.RouteGlobal.ADD_PET_DATA_ROUTE,
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
                route = NavigationKeys.RouteGlobal.ADD_REMINDER_ROUTE,
                arguments = listOf(navArgument(NavigationKeys.Arg.PET_ID_KEY) { type = NavType.StringType })
            ) {
                AddReminderDestination(navController, petId = it.arguments?.getString(NavigationKeys.Arg.PET_ID_KEY) ?: "")
            }
        }
    }

}