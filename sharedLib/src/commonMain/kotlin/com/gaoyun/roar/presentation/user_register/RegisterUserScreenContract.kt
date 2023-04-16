package com.gaoyun.roar.presentation.user_register

import com.gaoyun.roar.presentation.NavigationSideEffect
import com.gaoyun.roar.presentation.ViewEvent
import com.gaoyun.roar.presentation.ViewSideEffect
import com.gaoyun.roar.presentation.ViewState

class RegisterUserScreenContract {

    sealed class Event : ViewEvent {
        data class RegistrationSuccessful(val name: String, val userId: String) : Event()
    }

    object State: ViewState

    sealed class Effect : ViewSideEffect {
        sealed class Navigation : Effect(), NavigationSideEffect {
            object ToPetAdding : Navigation()
        }
    }

}