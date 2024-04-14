package com.gaoyun.roar.ui

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.gaoyun.roar.presentation.LAUNCH_LISTEN_FOR_EFFECTS
import com.gaoyun.roar.ui.features.onboarding.OnboardingRootScreen
import com.gaoyun.roar.ui.navigation.NavigationAction
import com.gaoyun.roar.ui.navigation.NavigationKeys
import com.gaoyun.roar.ui.theme.RoarTheme
import kotlinx.coroutines.flow.onEach
import moe.tlaster.precompose.koin.koinViewModel
import moe.tlaster.precompose.navigation.NavHost
import moe.tlaster.precompose.navigation.PopUpTo
import moe.tlaster.precompose.navigation.rememberNavigator

@Composable
fun App(
    colors: ColorScheme,
    isOnboardingComplete: Boolean,
) {
    RoarTheme(colors) {
        Surface(tonalElevation = RoarTheme.BACKGROUND_SURFACE_ELEVATION) {
            GlobalDestinationState(isOnboardingComplete = isOnboardingComplete)
        }
    }
}

@Composable
fun GlobalDestinationState(isOnboardingComplete: Boolean) {
    val viewModel = koinViewModel(vmClass = AppViewModel::class)

    val navigator = rememberNavigator()

    LaunchedEffect(LAUNCH_LISTEN_FOR_EFFECTS) {
        viewModel.navigationEffect.onEach { destination ->
            when (destination) {
                is NavigationAction.NavigateTo -> navigator.navigate(destination.path)
                is NavigationAction.NavigateBack -> navigator.goBack() //was `navigateUp()`
                is NavigationAction.PopTo -> navigator.goBack(
                    PopUpTo(
                        route = destination.path,
                        inclusive = destination.inclusive
                    )
                )
            }
        }.collect{
            println("GlobalDestination NavigationAction: $it")
        }
    }

    val initialRoute = if (isOnboardingComplete) {
        NavigationKeys.Route.HOME_ROUTE
    } else {
        NavigationKeys.Route.ONBOARDING_ROUTE
    }

    NavHost(
        navigator = navigator,
        initialRoute = initialRoute
    ) {
        scene(NavigationKeys.Route.ONBOARDING_ROUTE) {
            OnboardingRootScreen(navHostController = navigator)
        }
    }
}
