package com.gaoyun.feature_home_screen

import androidx.compose.foundation.layout.Box
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavHostController
import com.gaoyun.common.NavigationKeys
import com.gaoyun.common.OnLifecycleEvent
import com.gaoyun.common.ui.Loader
import com.gaoyun.roar.presentation.LAUNCH_LISTEN_FOR_EFFECTS
import com.gaoyun.roar.presentation.home_screen.HomeScreenContract
import com.gaoyun.roar.presentation.home_screen.HomeScreenViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.compose.getViewModel

@Composable
fun HomeScreenDestination(navHostController: NavHostController) {
    val viewModel: HomeScreenViewModel = getViewModel()
    val state = viewModel.viewState.collectAsState().value

    OnLifecycleEvent { _, event ->
        if (event == Lifecycle.Event.ON_RESUME) {
            viewModel.checkUserRegistered()
        }
    }

    HomeScreen(
        state = state,
        effectFlow = viewModel.effect,
        onEventSent = { event -> viewModel.setEvent(event) },
        onNavigationRequested = { navigationEffect ->
            when (navigationEffect) {
                is HomeScreenContract.Effect.Navigation.ToUserRegistration -> navHostController.navigate(NavigationKeys.RouteGlobal.REGISTER_USER_ROUTE)
                is HomeScreenContract.Effect.Navigation.ToAddPet -> navHostController.navigate(NavigationKeys.RouteGlobal.ADD_PET_ROUTE)
                is HomeScreenContract.Effect.Navigation.ToAddReminder -> navHostController.navigate("${NavigationKeys.RouteGlobal.ADD_REMINDER_ROUTE_BUILDER}/${navigationEffect.pet.id}")
                is HomeScreenContract.Effect.Navigation.NavigateBack -> navHostController.popBackStack()
            }
        },
        viewModel = viewModel
    )

}

@Composable
fun HomeScreen(
    state: HomeScreenContract.State,
    effectFlow: Flow<HomeScreenContract.Effect>,
    onEventSent: (event: HomeScreenContract.Event) -> Unit,
    onNavigationRequested: (navigationEffect: HomeScreenContract.Effect.Navigation) -> Unit,
    viewModel: HomeScreenViewModel
) {
    val rememberedState = rememberScaffoldState()

    LaunchedEffect(LAUNCH_LISTEN_FOR_EFFECTS) {
        effectFlow.onEach { effect ->
            when (effect) {
                is HomeScreenContract.Effect.Navigation.ToUserRegistration -> onNavigationRequested(effect)
                is HomeScreenContract.Effect.Navigation.ToAddPet -> onNavigationRequested(effect)
                is HomeScreenContract.Effect.Navigation.ToAddReminder -> onNavigationRequested(effect)
                is HomeScreenContract.Effect.Navigation.NavigateBack -> onNavigationRequested(effect)
            }
        }.collect()
    }

    Scaffold(scaffoldState = rememberedState) {
        Box {
            state.user?.let { user ->
                if (state.pets.isNotEmpty()) {
                    HomeState(
                        userName = user.name,
                        pets = state.pets,
                        onAddPetButtonClick = viewModel::openAddPetScreen,
                        onPetCardClick = viewModel::openAddReminderScreen
                    )
                } else {
                    NoPetsState(userName = user.name, viewModel::openAddPetScreen)
                }
            } ?: NoUserState(viewModel::openRegistration)

            Loader(isLoading = state.isLoading)
        }
    }
}