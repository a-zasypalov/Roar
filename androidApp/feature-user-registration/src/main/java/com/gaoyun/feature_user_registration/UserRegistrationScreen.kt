package com.gaoyun.feature_user_registration

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.gaoyun.common.theme.RoarTheme
import com.gaoyun.common.ui.PrimaryElevatedButton
import com.gaoyun.common.ui.SurfaceScaffold
import com.gaoyun.common.ui.TextFormField
import com.gaoyun.roar.presentation.LAUNCH_LISTEN_FOR_EFFECTS
import com.gaoyun.roar.presentation.user_register.RegisterUserScreenContract
import com.gaoyun.roar.presentation.user_register.RegisterUserViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.compose.getViewModel

@Composable
fun UserRegistrationDestination(navHostController: NavHostController) {
    val viewModel: RegisterUserViewModel = getViewModel()
    val state = viewModel.viewState.collectAsState().value

    UserRegistrationScreen(
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
@OptIn(ExperimentalMaterial3Api::class)
fun UserRegistrationScreen(
    state: RegisterUserScreenContract.State,
    effectFlow: Flow<RegisterUserScreenContract.Effect>,
    onEventSent: (event: RegisterUserScreenContract.Event) -> Unit,
    onNavigationRequested: (navigationEffect: RegisterUserScreenContract.Effect.Navigation) -> Unit,
) {
    LaunchedEffect(LAUNCH_LISTEN_FOR_EFFECTS) {
        effectFlow.onEach { effect ->
            when (effect) {
                is RegisterUserScreenContract.Effect.Navigation -> onNavigationRequested(effect)
                else -> {}
            }
        }.collect()
    }


    SurfaceScaffold {
        UserRegistrationForm { name ->
            onEventSent(RegisterUserScreenContract.Event.RegisterButtonClick(name))
        }
    }
}

@Composable
fun UserRegistrationForm(
    onRegisterClick: (String) -> Unit
) {
    val focusManager = LocalFocusManager.current
    val nameState = rememberSaveable { mutableStateOf("") }

    Box(contentAlignment = Alignment.BottomCenter) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxHeight()
        ) {
            Text("We just need your name")

            Spacer(modifier = Modifier.size(16.dp))

            TextFormField(
                text = nameState.value,
                leadingIcon = {
                    Icon(
                        Icons.Filled.Person,
                        "Name",
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                },
                label = "Name",
                onChange = {
                    nameState.value = it
                },
                imeAction = ImeAction.Done,
                modifier = Modifier.padding(horizontal = 24.dp)
            )
        }

        PrimaryElevatedButton(
            text = "Register",
            onClick = { onRegisterClick(nameState.value) },
            modifier = Modifier.padding(bottom = 32.dp)
        )
    }
}

@Composable
@Preview
fun UserRegistrationFormPreview() {
    RoarTheme {
        UserRegistrationForm {}
    }
}