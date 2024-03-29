package com.gaoyun.feature_user_registration

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.res.stringResource
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.gaoyun.common.R
import com.gaoyun.common.composables.SurfaceScaffold
import com.gaoyun.roar.presentation.LAUNCH_LISTEN_FOR_EFFECTS
import com.gaoyun.roar.presentation.NavigationSideEffect
import com.gaoyun.roar.presentation.user_register.RegisterUserScreenContract
import com.gaoyun.roar.presentation.user_register.RegisterUserViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.compose.getViewModel

@Composable
fun UserRegistrationDestination(onNavigationCall: (NavigationSideEffect) -> Unit) {
    val viewModel: RegisterUserViewModel = getViewModel()

    //Block back action
    BackHandler {}

    LaunchedEffect(LAUNCH_LISTEN_FOR_EFFECTS) {
        viewModel.effect.onEach { effect ->
            when (effect) {
                is RegisterUserScreenContract.Effect.Navigation -> onNavigationCall(effect)
            }
        }.collect()
    }

    val defaultUsername = stringResource(id = R.string.username)

    val signInLauncher = rememberLauncherForActivityResult(FirebaseAuthUIActivityResultContract()) { res ->
        if (res.resultCode == Activity.RESULT_OK) {
            Firebase.auth.currentUser?.let { user ->
                val nameToRegister = user.displayName ?: defaultUsername
                viewModel.setEvent(RegisterUserScreenContract.Event.RegistrationSuccessful(nameToRegister, user.uid))
            }
        }
    }

    SurfaceScaffold {
        UserRegistrationForm(
            { signInLauncher.launch(AuthUIConfig) },
            { viewModel.setEvent(RegisterUserScreenContract.Event.RegistrationSuccessful("Tester", "tester")) }
        )
    }
}

private val AuthUIConfig = AuthUI.getInstance()
    .createSignInIntentBuilder()
    .setAvailableProviders(arrayListOf(AuthUI.IdpConfig.GoogleBuilder().build()))
    .setLogo(R.drawable.ic_tab_home)
    .setTheme(R.style.RoarTheme)
    .setIsSmartLockEnabled(false)
    .build()