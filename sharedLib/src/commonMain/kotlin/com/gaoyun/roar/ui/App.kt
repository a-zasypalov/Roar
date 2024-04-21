package com.gaoyun.roar.ui

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.gaoyun.roar.presentation.LAUNCH_LISTEN_FOR_EFFECTS
import com.gaoyun.roar.ui.features.add_pet.AddPetAvatarDestination
import com.gaoyun.roar.ui.features.add_pet.AddPetPetTypeDestination
import com.gaoyun.roar.ui.features.add_pet.AddPetSetupDestination
import com.gaoyun.roar.ui.features.add_pet.pet_data.AddPetDataDestination
import com.gaoyun.roar.ui.features.home.HomeScreenDestination
import com.gaoyun.roar.ui.features.interactions.InteractionScreenDestination
import com.gaoyun.roar.ui.features.onboarding.OnboardingRootScreen
import com.gaoyun.roar.ui.features.pet.PetScreenDestination
import com.gaoyun.roar.ui.features.registration.UserRegistrationDestination
import com.gaoyun.roar.ui.navigation.NavigationAction
import com.gaoyun.roar.ui.navigation.NavigationKeys
import com.gaoyun.roar.ui.theme.RoarTheme
import kotlinx.coroutines.flow.onEach
import moe.tlaster.precompose.PreComposeApp
import moe.tlaster.precompose.koin.koinViewModel
import moe.tlaster.precompose.navigation.NavHost
import moe.tlaster.precompose.navigation.PopUpTo
import moe.tlaster.precompose.navigation.path
import moe.tlaster.precompose.navigation.rememberNavigator

@Composable
fun App(
    colors: ColorScheme,
    isOnboardingComplete: Boolean,
) {
    PreComposeApp {
        RoarTheme(colors) {
            Surface(tonalElevation = RoarTheme.BACKGROUND_SURFACE_ELEVATION) {
                GlobalDestinationState(isOnboardingComplete = isOnboardingComplete)
            }
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
        }.collect {
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
        scene(NavigationKeys.Route.HOME_ROUTE) {
            HomeScreenDestination(onNavigationCall = viewModel::navigate)
        }
        scene(NavigationKeys.Route.REGISTER_USER_ROUTE) {
            UserRegistrationDestination(onNavigationCall = viewModel::navigate)
        }

        scene(NavigationKeys.Route.PET_DETAIL_ROUTE) {
            PetScreenDestination(
                viewModel::navigate,
                petId = it.path<String>(NavigationKeys.Arg.PET_ID_KEY) ?: ""
            )
        }

        scene(NavigationKeys.Route.ADD_PET_ROUTE) {
            AddPetPetTypeDestination(onNavigationCall = viewModel::navigate)
        }

        scene(NavigationKeys.Route.ADD_PET_AVATAR_ROUTE) {
            AddPetAvatarDestination(
                petType = it.path<String>(NavigationKeys.Arg.PET_TYPE_KEY) ?: "",
                onNavigationCall = viewModel::navigate
            )
        }

        scene(NavigationKeys.Route.ADD_PET_DATA_ROUTE) {
            AddPetDataDestination(
                onNavigationCall = viewModel::navigate,
                petType = it.path<String>(NavigationKeys.Arg.PET_TYPE_KEY) ?: "",
                avatar = it.path<String>(NavigationKeys.Arg.AVATAR_KEY) ?: "",
            )
        }

        scene(NavigationKeys.Route.ADD_PET_SETUP_ROUTE) {
            AddPetSetupDestination(
                onNavigationCall = viewModel::navigate,
                petId = it.path<String>(NavigationKeys.Arg.PET_ID_KEY) ?: ""
            )
        }

        scene(NavigationKeys.Route.INTERACTION_DETAIL_ROUTE) {
            InteractionScreenDestination(
                onNavigationCall = viewModel::navigate,
                interactionId = it.path<String>(NavigationKeys.Arg.INTERACTION_ID_KEY) ?: "",
            )
        }
    }
}
