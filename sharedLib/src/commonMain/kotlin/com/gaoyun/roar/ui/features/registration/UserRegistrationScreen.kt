package com.gaoyun.roar.ui.features.registration

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.gaoyun.roar.presentation.LAUNCH_LISTEN_FOR_EFFECTS
import com.gaoyun.roar.presentation.NavigationSideEffect
import com.gaoyun.roar.presentation.user_register.RegisterUserScreenContract
import com.gaoyun.roar.presentation.user_register.RegisterUserViewModel
import com.gaoyun.roar.ui.SurfaceScaffold
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import moe.tlaster.precompose.koin.koinViewModel
import moe.tlaster.precompose.navigation.BackHandler

@Composable
fun UserRegistrationDestination(onNavigationCall: (NavigationSideEffect) -> Unit) {
    val viewModel = koinViewModel(vmClass = RegisterUserViewModel::class)


    //Block back action
    BackHandler {}

    LaunchedEffect(LAUNCH_LISTEN_FOR_EFFECTS) {
        viewModel.effect.onEach { effect ->
            when (effect) {
                is RegisterUserScreenContract.Effect.Navigation -> onNavigationCall(effect)
            }
        }.collect()
    }

    val  registrationLauncher = viewModel.registrationLauncher.launcher { username: String, id: String ->
        viewModel.setEvent(RegisterUserScreenContract.Event.RegistrationSuccessful(username, id))
    }

    SurfaceScaffold {
        UserRegistrationForm(
            { registrationLauncher() },
            {
                viewModel.setEvent(
                    RegisterUserScreenContract.Event.RegistrationSuccessful(
                        "Tester",
                        "tester"
                    )
                )
            }
        )
    }
}

interface RegistrationLauncher {
    @Composable fun launcher(registrationSuccessfulCallback: (String, String) -> Unit): () -> Unit
}