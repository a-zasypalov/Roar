package com.gaoyun.feature_user_registration

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.gaoyun.common.NavigationKeys.Route.HOME_ROUTE
import com.gaoyun.common.R
import com.gaoyun.common.composables.SurfaceScaffold
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

    //Block back action
    BackHandler {}

    UserRegistrationScreen(
        effectFlow = viewModel.effect,
        onEventSent = { event -> viewModel.setEvent(event) },
        onNavigationRequested = { navigationEffect ->
            when (navigationEffect) {
                is RegisterUserScreenContract.Effect.Navigation.ToPetAdding -> navHostController.navigate(HOME_ROUTE)
            }
        },
    )
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun UserRegistrationScreen(
    effectFlow: Flow<RegisterUserScreenContract.Effect>,
    onEventSent: (event: RegisterUserScreenContract.Event) -> Unit,
    onNavigationRequested: (navigationEffect: RegisterUserScreenContract.Effect.Navigation) -> Unit,
) {
    LaunchedEffect(LAUNCH_LISTEN_FOR_EFFECTS) {
        effectFlow.onEach { effect ->
            when (effect) {
                is RegisterUserScreenContract.Effect.Navigation -> onNavigationRequested(effect)
            }
        }.collect()
    }

    val defaultUsername = stringResource(id = R.string.username)
    var nameState by rememberSaveable { mutableStateOf("") }

    val signInLauncher = rememberLauncherForActivityResult(FirebaseAuthUIActivityResultContract()) { res ->
        if (res.resultCode == Activity.RESULT_OK) {
            Firebase.auth.currentUser?.let { user ->
                val nameToRegister = nameState.ifEmpty { user.displayName ?: defaultUsername }
                onEventSent(RegisterUserScreenContract.Event.RegistrationSuccessful(nameToRegister, user.uid))
            }
        }
    }

    SurfaceScaffold {
        UserRegistrationForm { name ->
            nameState = name
            signInLauncher.launch(AuthUIConfig)
        }
    }
}

private val AuthUIConfig = AuthUI.getInstance()
    .createSignInIntentBuilder()
    .setAvailableProviders(arrayListOf(AuthUI.IdpConfig.GoogleBuilder().build()))
    .setLogo(R.drawable.ic_tab_home)
    .setTheme(R.style.RoarTheme)
    .setIsSmartLockEnabled(false)
    .build()