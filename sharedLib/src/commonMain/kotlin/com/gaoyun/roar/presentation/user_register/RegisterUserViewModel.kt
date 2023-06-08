package com.gaoyun.roar.presentation.user_register

import com.gaoyun.roar.domain.user.RegisterUserUseCase
import com.gaoyun.roar.network.SynchronisationApi
import com.gaoyun.roar.presentation.BaseViewModel
import kotlinx.coroutines.launch

class RegisterUserViewModel(
    private val registerUserUseCase: RegisterUserUseCase,
    private val syncApi: SynchronisationApi,
) : BaseViewModel<RegisterUserScreenContract.Event, RegisterUserScreenContract.State, RegisterUserScreenContract.Effect>() {

    override fun setInitialState() = RegisterUserScreenContract.State

    override fun handleEvents(event: RegisterUserScreenContract.Event) {
        when (event) {
            is RegisterUserScreenContract.Event.RegistrationSuccessful -> registerUser(event.name, event.userId)
        }
    }

    private fun registerUser(name: String, id: String) = scope.launch {
        registerUserUseCase.register(name, id)
        syncApi.retrieveBackup {
            setEffect { RegisterUserScreenContract.Effect.Navigation.ToPetAdding }
        }
    }
}