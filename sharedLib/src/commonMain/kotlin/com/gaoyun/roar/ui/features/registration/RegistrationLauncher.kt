package com.gaoyun.roar.ui.features.registration

import androidx.compose.runtime.Composable

interface RegistrationLauncher {
    fun launcher(registrationSuccessfulCallback: (String, String) -> Unit): () -> Unit
}

interface RegistrationLauncherApple: RegistrationLauncher {
    fun launcherApple(registrationSuccessfulCallback: (String, String) -> Unit): () -> Unit
}

interface RegistrationLauncherComposable : RegistrationLauncher {
    @Composable
    fun launcherComposable(registrationSuccessfulCallback: (String, String) -> Unit): () -> Unit
}