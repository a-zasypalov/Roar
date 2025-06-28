package com.gaoyun.roar.presentation.user_register

import androidx.lifecycle.viewModelScope
import com.gaoyun.roar.domain.user.RegisterUserUseCase
import com.gaoyun.roar.network.SynchronisationApi
import com.gaoyun.roar.presentation.BaseViewModel
import com.gaoyun.roar.ui.features.registration.RegistrationLauncher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class RegisterUserScreenState(
    val isLoading: Boolean = false
)

class RegisterUserViewModel(
    private val registerUserUseCase: RegisterUserUseCase,
    private val syncApi: SynchronisationApi,
    val registrationLauncher: RegistrationLauncher,
) : BaseViewModel() {

    override val viewState = MutableStateFlow(RegisterUserScreenState())

    fun registerUser(name: String, id: String, onComplete: () -> Unit) = viewModelScope.launch {
        viewState.update { it.copy(isLoading = true) }
        registerUserUseCase.register(name, id)
        syncApi.retrieveBackup(
            onFinish = {
                viewState.update { it.copy(isLoading = false) }
                onComplete()
            }
        )
    }
}