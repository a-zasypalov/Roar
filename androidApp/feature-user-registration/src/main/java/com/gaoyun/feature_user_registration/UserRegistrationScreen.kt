package com.gaoyun.feature_user_registration

import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.OnLifecycleEvent
import androidx.navigation.NavHostController
import com.gaoyun.common.NavigationKeys
import com.gaoyun.common.OnLifecycleEvent
import com.gaoyun.roar.presentation.home_screen.HomeScreenContract
import com.gaoyun.roar.presentation.home_screen.HomeScreenViewModel
import com.gaoyun.roar.presentation.user_register.RegisterUserScreenContract
import com.gaoyun.roar.presentation.user_register.RegisterUserViewModel
import kotlinx.coroutines.flow.Flow
import org.koin.androidx.compose.getViewModel

@Composable
fun UserRegistrationDestination(navHostController: NavHostController) {
    val viewModel: RegisterUserViewModel = getViewModel()
    val state = viewModel.viewState.collectAsState().value

    UserRegistrationForm(
        state = state,
        effectFlow = viewModel.effect,
        onEventSent = { event -> viewModel.setEvent(event) },
        onNavigationRequested = { navigationEffect ->
            when (navigationEffect) {
                is RegisterUserScreenContract.Effect.Navigation.ToPetAdding -> navHostController.navigate("")
                is RegisterUserScreenContract.Effect.Navigation.NavigateBack -> navHostController.navigateUp()
            }
        },
    )

}

@Composable
fun UserRegistrationForm(
    state: RegisterUserScreenContract.State,
    effectFlow: Flow<RegisterUserScreenContract.Effect>,
    onEventSent: (event: RegisterUserScreenContract.Event) -> Unit,
    onNavigationRequested: (navigationEffect: RegisterUserScreenContract.Effect.Navigation) -> Unit,
) {
    val rememberedState = rememberScaffoldState()

    Scaffold(scaffoldState = rememberedState) {
        Text("User registration")
    }
}