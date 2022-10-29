package com.gaoyun.roar.presentation.home_screen

import com.gaoyun.roar.model.domain.Pet
import com.gaoyun.roar.model.domain.User
import com.gaoyun.roar.presentation.ViewEvent
import com.gaoyun.roar.presentation.ViewSideEffect
import com.gaoyun.roar.presentation.ViewState

class HomeScreenContract {

    sealed class Event : ViewEvent {
    }

    data class State(
        val user: User? = null,
        val pets: List<Pet> = emptyList(),
        val isLoading: Boolean = false
    ) : ViewState

    sealed class Effect : ViewSideEffect {
//        object UserRegistered: Effect()

        sealed class Navigation : Effect() {
            object NavigateBack : Navigation()
            object ToUserRegistration : Navigation()
            object ToAddPet : Navigation()
        }
    }
}