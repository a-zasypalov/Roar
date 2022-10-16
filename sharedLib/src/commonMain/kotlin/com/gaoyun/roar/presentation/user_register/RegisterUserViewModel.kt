package com.gaoyun.roar.presentation.user_register

import com.gaoyun.roar.domain.RegisterUserUseCase
import com.gaoyun.roar.presentation.BaseViewModel
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class RegisterUserViewModel :
    BaseViewModel<RegisterUserScreenContract.Event, RegisterUserScreenContract.State, RegisterUserScreenContract.Effect>(),
    KoinComponent {

    private val registerUserUseCase: RegisterUserUseCase by inject()

    override fun setInitialState() = RegisterUserScreenContract.State

    override fun handleEvents(event: RegisterUserScreenContract.Event) {
        when (event) {
            is RegisterUserScreenContract.Event.RegisterButtonClick -> registerUser(event.name)
        }
    }

    private fun registerUser(name: String) = scope.launch {
        registerUserUseCase.register(name)
        setEffect { RegisterUserScreenContract.Effect.UserRegistered }
    }
}