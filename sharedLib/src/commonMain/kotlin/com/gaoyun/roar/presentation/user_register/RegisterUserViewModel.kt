package com.gaoyun.roar.presentation.user_register

import com.gaoyun.roar.domain.user.RegisterUserUseCase
import com.gaoyun.roar.network.SynchronisationApi
import com.gaoyun.roar.presentation.BaseViewModel
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class RegisterUserViewModel :
    BaseViewModel<RegisterUserScreenContract.Event, RegisterUserScreenContract.State, RegisterUserScreenContract.Effect>(),
    KoinComponent {

    private val registerUserUseCase: RegisterUserUseCase by inject()
    private val syncApi: SynchronisationApi by inject()

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