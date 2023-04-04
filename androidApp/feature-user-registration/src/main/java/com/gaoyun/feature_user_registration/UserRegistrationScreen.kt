package com.gaoyun.feature_user_registration

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.gaoyun.common.NavigationKeys.Route.HOME_ROUTE
import com.gaoyun.common.R
import com.gaoyun.common.theme.RoarTheme
import com.gaoyun.common.ui.PrimaryElevatedButton
import com.gaoyun.common.ui.SurfaceScaffold
import com.gaoyun.common.ui.TextFormField
import com.gaoyun.roar.presentation.LAUNCH_LISTEN_FOR_EFFECTS
import com.gaoyun.roar.presentation.user_register.RegisterUserScreenContract
import com.gaoyun.roar.presentation.user_register.RegisterUserViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
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
                is RegisterUserScreenContract.Effect.Navigation.ToPetAdding -> navHostController.navigate(HOME_ROUTE)
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
    val nameState = rememberSaveable { mutableStateOf("") }

    LaunchedEffect(LAUNCH_LISTEN_FOR_EFFECTS) {
        effectFlow.onEach { effect ->
            when (effect) {
                is RegisterUserScreenContract.Effect.Navigation -> onNavigationRequested(effect)
                else -> {}
            }
        }.collect()
    }

    BackHandler {}

    val signInLauncher = rememberLauncherForActivityResult(
        FirebaseAuthUIActivityResultContract()
    ) { res ->
        if(res.resultCode == Activity.RESULT_OK) {
            Firebase.auth.currentUser?.let { user ->
                onEventSent(RegisterUserScreenContract.Event.RegisterButtonClick(nameState.value, user.uid))
            }
        }
    }

    SurfaceScaffold {
        UserRegistrationForm { name ->
            nameState.value = name

            signInLauncher.launch(
                AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setAvailableProviders(arrayListOf(AuthUI.IdpConfig.GoogleBuilder().build()))
                    .setLogo(R.drawable.ic_tab_home)
                    .setTheme(R.style.RoarTheme)
                    .setIsSmartLockEnabled(false)
                    .build()
            )
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
            Text(stringResource(id = R.string.registration_subtitle))

            Spacer(modifier = Modifier.size(16.dp))

            TextFormField(
                text = nameState.value,
                leadingIcon = {
                    Icon(
                        Icons.Filled.Person,
                        stringResource(id = R.string.name),
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                },
                label = stringResource(id = R.string.name),
                onChange = {
                    nameState.value = it
                },
                imeAction = ImeAction.Done,
                modifier = Modifier.padding(horizontal = 24.dp)
            )
        }

        PrimaryElevatedButton(
            text = stringResource(id = R.string.register),
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