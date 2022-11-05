package com.gaoyun.roar.android

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.gaoyun.common.NavigationKeys
import com.gaoyun.common.theme.RoarTheme
import com.gaoyun.feature_add_pet.AddPetDestination
import com.gaoyun.feature_create_reminder.AddReminderDestination
import com.gaoyun.feature_home_screen.HomeScreenDestination
import com.gaoyun.feature_user_registration.UserRegistrationDestination

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
                AddPetDestination(navHostController = navController)
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