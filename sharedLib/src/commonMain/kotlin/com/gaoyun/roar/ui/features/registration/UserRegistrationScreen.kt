package com.gaoyun.roar.ui.features.registration

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.gaoyun.roar.ui.common.composables.BoxWithLoader
import com.gaoyun.roar.ui.common.composables.SurfaceScaffold
import com.gaoyun.roar.ui.navigation.NavigationSideEffect
import com.gaoyun.roar.util.Platform
import com.gaoyun.roar.util.PlatformNames
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun UserRegistrationDestination(
    navigate: (NavigationSideEffect) -> Unit,
) {
    val viewModel = koinViewModel<RegisterUserViewModel>()
    val state by viewModel.viewState.collectAsState()

//    BackHandler { navigate(CloseAppNavigationSideEffect) }

    val registrationCallback: (String, String) -> Unit = { username, id ->
        viewModel.registerUser(username, id) {
            navigate(RegisterUserScreenContract.Effect.Navigation.ToPetAdding)
        }
    }

    val registrationLauncherGoogle = when (Platform.name) {
        PlatformNames.Android ->
            (viewModel.registrationLauncher as? RegistrationLauncherComposable)
                ?.launcherComposable(registrationCallback)

        PlatformNames.IOS ->
            (viewModel.registrationLauncher as? RegistrationLauncherApple)
                ?.launcher(registrationCallback)
    }

    val registrationLauncherApple = if (Platform.name == PlatformNames.IOS) {
        (viewModel.registrationLauncher as? RegistrationLauncherApple)
            ?.launcherApple(registrationCallback)
    } else null

    SurfaceScaffold {
        BoxWithLoader(isLoading = state.isLoading) {
            UserRegistrationForm(
                onRegisterClick = {
                    when (it) {
                        RegistrationType.Google -> registrationLauncherGoogle?.invoke()
                        RegistrationType.Apple -> registrationLauncherApple?.invoke()
                    }
                },
                onRegisterTestClick = {
                    viewModel.registerUser("Tester", "tester") {
                        navigate(RegisterUserScreenContract.Effect.Navigation.ToPetAdding)
                    }
                }
            )
        }
    }
}