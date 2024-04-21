package com.gaoyun.roar.android

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.firebase.ui.auth.AuthMethodPickerLayout
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.gaoyun.common.R
import com.gaoyun.roar.ui.features.registration.RegistrationLauncherComposable
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

private val AuthUIConfig = AuthUI.getInstance()
    .createSignInIntentBuilder()
    .setAvailableProviders(arrayListOf(AuthUI.IdpConfig.GoogleBuilder().build()))
    .setLogo(R.drawable.ic_tab_home)
    .setTheme(R.style.RoarTheme)
    .setIsSmartLockEnabled(false)
    .build()


object RegistrationLauncherAndroid : RegistrationLauncherComposable {
    @Composable
    override fun launcherComposable(registrationSuccessfulCallback: (String, String) -> Unit): () -> Unit {
        val defaultUsername = stringResource(id = R.string.username)

        val signInLauncher =
            rememberLauncherForActivityResult(FirebaseAuthUIActivityResultContract()) { res ->
                if (res.resultCode == Activity.RESULT_OK) {
                    Firebase.auth.currentUser?.let { user ->
                        registrationSuccessfulCallback(user.displayName ?: defaultUsername, user.uid)
                    }
                }
            }

        return { signInLauncher.launch(AuthUIConfig) }
    }

    override fun launcher(registrationSuccessfulCallback: (String, String) -> Unit): () -> Unit = {}
}