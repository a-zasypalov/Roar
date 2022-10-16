package com.gaoyun.roar.android

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.gaoyun.common.NavigationKeys
import com.gaoyun.common.theme.RoarTheme

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
        NavHost(navController = navController, startDestination = NavigationKeys.RouteGlobal.HOME_ROUTE) {
            composable(NavigationKeys.RouteGlobal.HOME_ROUTE) {

            }

//            composable(
//                route = NavigationKeys.RouteGlobal.FLIGHT_OFFERS_ROUTE_WITH_SEARCH_ID,
//                arguments = listOf(navArgument(NavigationKeys.Arg.SAVED_SEARCH_ID) { type = NavType.StringType })
//            ) {
//                FlightOffersScreenDestination(navController, it.arguments?.getString(NavigationKeys.Arg.SAVED_SEARCH_ID))
//            }
        }
    }

}