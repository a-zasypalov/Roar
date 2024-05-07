package com.gaoyun.roar.ui

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.VisibilityThreshold
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.gaoyun.roar.presentation.LAUNCH_LISTEN_FOR_EFFECTS
import com.gaoyun.roar.ui.features.about.AboutScreenDestination
import com.gaoyun.roar.ui.features.add_pet.AddPetAvatarDestination
import com.gaoyun.roar.ui.features.add_pet.AddPetPetTypeDestination
import com.gaoyun.roar.ui.features.add_pet.AddPetSetupDestination
import com.gaoyun.roar.ui.features.add_pet.pet_data.AddPetDataDestination
import com.gaoyun.roar.ui.features.create_reminder.AddReminderCompleteDestination
import com.gaoyun.roar.ui.features.create_reminder.AddReminderDestination
import com.gaoyun.roar.ui.features.create_reminder.setup.SetupReminderDestination
import com.gaoyun.roar.ui.features.home.HomeScreenDestination
import com.gaoyun.roar.ui.features.interactions.InteractionScreenDestination
import com.gaoyun.roar.ui.features.onboarding.OnboardingRootScreen
import com.gaoyun.roar.ui.features.pet.PetScreenDestination
import com.gaoyun.roar.ui.features.registration.UserRegistrationDestination
import com.gaoyun.roar.ui.features.user.edit_user.EditUserScreenDestination
import com.gaoyun.roar.ui.features.user.user_screen.UserScreenDestination
import com.gaoyun.roar.ui.navigation.NavigationAction
import com.gaoyun.roar.ui.navigation.NavigationKeys
import com.gaoyun.roar.ui.theme.RoarTheme
import com.gaoyun.roar.util.Platform
import com.gaoyun.roar.util.PlatformNames
import kotlinx.coroutines.flow.onEach
import moe.tlaster.precompose.PreComposeApp
import moe.tlaster.precompose.koin.koinViewModel
import moe.tlaster.precompose.navigation.NavHost
import moe.tlaster.precompose.navigation.PopUpTo
import moe.tlaster.precompose.navigation.SwipeProperties
import moe.tlaster.precompose.navigation.path
import moe.tlaster.precompose.navigation.rememberNavigator
import moe.tlaster.precompose.navigation.transition.NavTransition

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
                is NavigationAction.NavigateBack -> navigator.goBack()
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
        initialRoute = initialRoute,
        swipeProperties = if (Platform.name == PlatformNames.IOS) remember { SwipeProperties(
            spaceToSwipe = 16.dp,
            positionalThreshold = { distance: Float -> distance * 0.9f },
            velocityThreshold = { 0.dp.toPx() }
        ) } else null,
        navTransition = if (Platform.name == PlatformNames.IOS) {
            remember {
                NavTransition(
                    createTransition = fadeIn() + slideInHorizontally(
                        animationSpec = spring(
                            stiffness = Spring.StiffnessMediumLow,
                            visibilityThreshold = IntOffset.VisibilityThreshold
                        ),
                        initialOffsetX = { it }
                    ),
                    destroyTransition = fadeOut(targetAlpha = 0.5f) + slideOutHorizontally(
                        animationSpec = spring(
                            stiffness = Spring.StiffnessMediumLow,
                            visibilityThreshold = IntOffset.VisibilityThreshold
                        ),
                        targetOffsetX = { it }
                    ),
                    pauseTransition = fadeOut(targetAlpha = 0.5f) + slideOutHorizontally(
                        animationSpec = spring(
                            stiffness = Spring.StiffnessMediumLow,
                            visibilityThreshold = IntOffset.VisibilityThreshold
                        ),
                        targetOffsetX = { -it / 2 }
                    ),
                    resumeTransition = fadeIn() + slideInHorizontally(
                        animationSpec = spring(
                            stiffness = Spring.StiffnessMediumLow,
                            visibilityThreshold = IntOffset.VisibilityThreshold
                        ),
                        initialOffsetX = { -it / 2 }
                    ),
                    exitTargetContentZIndex = 1f
                )
            }
        } else NavTransition()
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

        scene(NavigationKeys.Route.USER_ROUTE) {
            UserScreenDestination(viewModel::navigate)
        }

        scene(NavigationKeys.Route.USER_EDIT_ROUTE) {
            EditUserScreenDestination(viewModel::navigate)
        }

        scene(NavigationKeys.Route.ABOUT_ROUTE) {
            AboutScreenDestination(viewModel::navigate)
        }

        scene(NavigationKeys.Route.PET_EDIT_AVATAR_ROUTE) {
            AddPetAvatarDestination(
                petType = it.path<String>(NavigationKeys.Arg.PET_TYPE_KEY) ?: "",
                petId = it.path<String>(NavigationKeys.Arg.PET_ID_KEY),
                onNavigationCall = viewModel::navigate
            )
        }

        scene(NavigationKeys.Route.PET_EDIT_ROUTE) {
            AddPetDataDestination(
                onNavigationCall = viewModel::navigate,
                petType = it.path<String>(NavigationKeys.Arg.PET_TYPE_KEY) ?: "",
                avatar = it.path<String>(NavigationKeys.Arg.AVATAR_KEY) ?: "",
                petId = it.path<String>(NavigationKeys.Arg.PET_ID_KEY)
            )
        }

        scene(NavigationKeys.Route.ADD_REMINDER_ROUTE) {
            AddReminderDestination(
                onNavigationCall = viewModel::navigate,
                petId = it.path<String>(NavigationKeys.Arg.PET_ID_KEY) ?: ""
            )
        }

        scene(NavigationKeys.Route.SETUP_REMINDER_ROUTE) {
            SetupReminderDestination(
                onNavigationCall = viewModel::navigate,
                petId = it.path<String>(NavigationKeys.Arg.PET_ID_KEY) ?: "",
                templateId = it.path<String>(NavigationKeys.Arg.TEMPLATE_ID_KEY) ?: "custom"
            )
        }

        scene(NavigationKeys.Route.EDIT_REMINDER_ROUTE) {
            SetupReminderDestination(
                onNavigationCall = viewModel::navigate,
                petId = it.path<String>(NavigationKeys.Arg.PET_ID_KEY) ?: "",
                templateId = it.path<String>(NavigationKeys.Arg.TEMPLATE_ID_KEY) ?: "custom",
                interactionId = it.path<String>(NavigationKeys.Arg.INTERACTION_ID_KEY)
            )
        }

        scene(NavigationKeys.Route.SETUP_REMINDER_COMPLETE_ROUTE) {
            AddReminderCompleteDestination(
                onNavigationCall = viewModel::navigate,
                petAvatar = it.path<String>(NavigationKeys.Arg.AVATAR_KEY) ?: "",
            )
        }
    }
}
