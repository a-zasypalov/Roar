package com.gaoyun.roar.presentation.add_pet.data

import com.gaoyun.roar.model.domain.PetType
import com.gaoyun.roar.presentation.ViewEvent
import com.gaoyun.roar.presentation.ViewSideEffect
import com.gaoyun.roar.presentation.ViewState
import kotlinx.datetime.LocalDate

class AddPetDataScreenContract {

    sealed class Event : ViewEvent {
        class PetDataInit(val petType: String, val avatar: String) : Event()
        class AddPetButtonClicked(
            val petType: String,
            val avatar: String,
            val breed: String,
            val name: String,
            val birthday: LocalDate,
            val isSterilized: Boolean
        ) : Event()
    }

    data class State(
        val petType: PetType? = null,
        val avatar: String? = null,
        val breeds: List<String> = listOf(),
        val isLoading: Boolean = false
    ) : ViewState

    sealed class Effect : ViewSideEffect {
        object PetAdded : Effect()

        sealed class Navigation : Effect() {
            object NavigateBack : Navigation()
        }
    }
}