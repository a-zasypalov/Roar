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
import androidx.compose.ui.unit.IntOffset
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.gaoyun.roar.ui.features.about.AboutScreenDestination
import com.gaoyun.roar.ui.features.add_pet.avatar.AddPetAvatarDestination
import com.gaoyun.roar.ui.features.add_pet.pet_type.AddPetPetTypeDestination
import com.gaoyun.roar.ui.features.add_pet.setup.AddPetSetupDestination
import com.gaoyun.roar.ui.features.add_pet.pet_data.AddPetDataDestination
import com.gaoyun.roar.ui.features.create_reminder.complete.AddReminderCompleteDestination
import com.gaoyun.roar.ui.features.create_reminder.choose_template.AddReminderDestination
import com.gaoyun.roar.ui.features.create_reminder.setup.SetupReminderDestination
import com.gaoyun.roar.ui.features.home.HomeScreenDestination
import com.gaoyun.roar.ui.features.interactions.InteractionScreenDestination
import com.gaoyun.roar.ui.features.onboarding.OnboardingRootScreen
import com.gaoyun.roar.ui.features.pet.PetScreenDestination
import com.gaoyun.roar.ui.features.registration.UserRegistrationDestination
import com.gaoyun.roar.ui.features.user.edit_user.EditUserScreenDestination
import com.gaoyun.roar.ui.features.user.user_screen.UserScreenDestination
import com.gaoyun.roar.ui.navigation.AddPetAvatarArgs
import com.gaoyun.roar.ui.navigation.AddPetDataArgs
import com.gaoyun.roar.ui.navigation.AddPetSetupArgs
import com.gaoyun.roar.ui.navigation.AddReminderArgs
import com.gaoyun.roar.ui.navigation.EditReminderArgs
import com.gaoyun.roar.ui.navigation.InteractionDetailArgs
import com.gaoyun.roar.ui.navigation.NavigationAction
import com.gaoyun.roar.ui.navigation.NavigationKeys
import com.gaoyun.roar.ui.navigation.PetDetailArgs
import com.gaoyun.roar.ui.navigation.PetEditArgs
import com.gaoyun.roar.ui.navigation.PetEditAvatarArgs
import com.gaoyun.roar.ui.navigation.SetupReminderArgs
import com.gaoyun.roar.ui.navigation.SetupReminderCompleteArgs
import com.gaoyun.roar.ui.navigation.appArgsTypeMap
import com.gaoyun.roar.ui.theme.RoarTheme
import com.gaoyun.roar.util.Platform
import com.gaoyun.roar.util.PlatformNames
import kotlinx.coroutines.flow.onEach
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun App(
    dynamicColorsScheme: ColorScheme?,
) {
    val viewModel = koinViewModel<AppViewModel>()
    val colors = dynamicColorsScheme ?: viewModel.getColorScheme()

    RoarTheme(colors) {
        Surface(tonalElevation = RoarTheme.BACKGROUND_SURFACE_ELEVATION) {
            GlobalDestinationState(viewModel.isOnboardingComplete(), viewModel)
        }
    }
}

@Composable
fun GlobalDestinationState(
    isOnboardingComplete: Boolean,
    viewModel: AppViewModel,
) {
    val navController = rememberNavController()

    LaunchedEffect("LAUNCH_LISTEN_FOR_EFFECTS") {
        viewModel.navigationEffect.onEach { action ->
            when (action) {
                is NavigationAction.NavigateBack -> navController.popBackStack()
                is NavigationAction.PopTo -> navController.popBackStack(action.path, action.inclusive)
                is NavigationAction.NavigateToPath -> navController.navigate(action.path)
                is NavigationAction.NavigateTo<*> -> navController.navigate(action.args)
                is NavigationAction.NavigateToWithBackHandler<*, *> -> {
                    navController.navigate(action.args) { popUpTo(action.popupTo) { inclusive = action.inclusive } }
                }

                is NavigationAction.NavigateToWithPathBackHandler<*> -> {
                    navController.navigate(action.args) { popUpTo(route = action.popupTo) { inclusive = action.inclusive } }
                }

                is NavigationAction.NavigateToPathWithBackHandler -> {
                    navController.navigate(action.path) { popUpTo(action.popupTo) { inclusive = action.inclusive } }
                }
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

    NavigationGraph(navController, viewModel, initialRoute)
}

@Composable
fun NavigationGraph(
    navController: NavHostController,
    viewModel: AppViewModel,
    initialRoute: String,
) {
    val paths: NavGraphBuilder.() -> Unit = { AppNavigationPaths(navController, viewModel) }

    when (Platform.name) {
        PlatformNames.IOS -> NavHost(
            navController = navController,
            startDestination = initialRoute,
            builder = paths
        )

        PlatformNames.Android -> NavHost(
            navController = navController,
            startDestination = initialRoute,
            enterTransition = {
                fadeIn() + slideInHorizontally(
                    animationSpec = spring(
                        stiffness = Spring.StiffnessMediumLow,
                        visibilityThreshold = IntOffset.VisibilityThreshold
                    ),
                    initialOffsetX = { it }
                )
            },
            exitTransition = {
                fadeOut(targetAlpha = 0f) + slideOutHorizontally(
                    animationSpec = spring(
                        stiffness = Spring.StiffnessMediumLow,
                        visibilityThreshold = IntOffset.VisibilityThreshold
                    ),
                    targetOffsetX = { -it }
                )
            },
            popEnterTransition = {
                fadeIn() + slideInHorizontally(
                    animationSpec = spring(
                        stiffness = Spring.StiffnessMediumLow,
                        visibilityThreshold = IntOffset.VisibilityThreshold
                    ),
                    initialOffsetX = { -it }
                )
            },
            popExitTransition = {
                fadeOut(targetAlpha = 0f) + slideOutHorizontally(
                    animationSpec = spring(
                        stiffness = Spring.StiffnessMediumLow,
                        visibilityThreshold = IntOffset.VisibilityThreshold
                    ),
                    targetOffsetX = { it }
                )
            },
            builder = paths
        )
    }
}

internal val AppNavigationPaths: NavGraphBuilder.(
    NavHostController,
    AppViewModel,
) -> Unit = { navController, viewModel ->

    composable(NavigationKeys.Route.ONBOARDING_ROUTE) {
        OnboardingRootScreen(navHostController = navController)
    }

    composable(NavigationKeys.Route.HOME_ROUTE) {
        HomeScreenDestination(onNavigationCall = viewModel::navigate)
    }

    composable(NavigationKeys.Route.REGISTER_USER_ROUTE) {
        UserRegistrationDestination(navigate = viewModel::navigate)
    }

    composable<PetDetailArgs>(typeMap = appArgsTypeMap) {
        val args = it.toRoute<PetDetailArgs>()
        PetScreenDestination(
            navigate = viewModel::navigate,
            petId = args.petId
        )
    }

    composable(NavigationKeys.Route.ADD_PET_ROUTE) {
        AddPetPetTypeDestination(navigate = viewModel::navigate)
    }

    composable<AddPetAvatarArgs>(typeMap = appArgsTypeMap) {
        val args = it.toRoute<AddPetAvatarArgs>()
        AddPetAvatarDestination(
            petType = args.petType,
            navigate = viewModel::navigate
        )
    }

    composable<AddPetDataArgs>(typeMap = appArgsTypeMap) {
        val args = it.toRoute<AddPetDataArgs>()
        AddPetDataDestination(
            navigate = viewModel::navigate,
            petType = args.petType,
            avatar = args.avatar,
        )
    }

    composable<AddPetSetupArgs>(typeMap = appArgsTypeMap) {
        val args = it.toRoute<AddPetSetupArgs>()
        AddPetSetupDestination(
            navigate = viewModel::navigate,
            petId = args.petId
        )
    }

    composable<InteractionDetailArgs>(typeMap = appArgsTypeMap) {
        val args = it.toRoute<InteractionDetailArgs>()
        InteractionScreenDestination(
            navigate = viewModel::navigate,
            interactionId = args.interactionId
        )
    }

    composable(NavigationKeys.Route.USER_ROUTE) {
        UserScreenDestination(navigate = viewModel::navigate)
    }

    composable(NavigationKeys.Route.USER_EDIT_ROUTE) {
        EditUserScreenDestination(navigate = viewModel::navigate)
    }

    composable(NavigationKeys.Route.ABOUT_ROUTE) {
        AboutScreenDestination(navigate = viewModel::navigate)
    }

    composable<PetEditAvatarArgs>(typeMap = appArgsTypeMap) {
        val args = it.toRoute<PetEditAvatarArgs>()
        AddPetAvatarDestination(
            petType = args.petType,
            petId = args.petId,
            navigate = viewModel::navigate
        )
    }

    composable<PetEditArgs>(typeMap = appArgsTypeMap) {
        val args = it.toRoute<PetEditArgs>()
        AddPetDataDestination(
            navigate = viewModel::navigate,
            petType = args.petType,
            avatar = args.avatar,
            petId = args.petId
        )
    }

    composable<AddReminderArgs>(typeMap = appArgsTypeMap) {
        val args = it.toRoute<AddReminderArgs>()
        AddReminderDestination(
            navigate = viewModel::navigate,
            petId = args.petId
        )
    }

    composable<SetupReminderArgs>(typeMap = appArgsTypeMap) {
        val args = it.toRoute<SetupReminderArgs>()
        SetupReminderDestination(
            navigate = viewModel::navigate,
            petId = args.petId,
            templateId = args.templateId
        )
    }

    composable<EditReminderArgs>(typeMap = appArgsTypeMap) {
        val args = it.toRoute<EditReminderArgs>()
        SetupReminderDestination(
            navigate = viewModel::navigate,
            petId = args.petId,
            templateId = args.templateId ?: "custom",
            interactionId = args.interactionId
        )
    }

    composable<SetupReminderCompleteArgs>(typeMap = appArgsTypeMap) {
        val args = it.toRoute<SetupReminderCompleteArgs>()
        AddReminderCompleteDestination(
            navigate = viewModel::navigate,
            petAvatar = args.avatar,
        )
    }
}