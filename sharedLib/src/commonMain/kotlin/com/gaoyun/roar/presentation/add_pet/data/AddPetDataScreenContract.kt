package com.gaoyun.roar.presentation.add_pet.data

import com.gaoyun.roar.model.domain.Pet
import com.gaoyun.roar.model.domain.PetType
import com.gaoyun.roar.presentation.NavigationSideEffect
import com.gaoyun.roar.presentation.ViewEvent
import com.gaoyun.roar.presentation.ViewSideEffect
import com.gaoyun.roar.presentation.ViewState
import kotlinx.datetime.LocalDate

class AddPetDataScreenContract {

    sealed class Event : ViewEvent {
        class PetDataInit(val petType: String, val avatar: String, val petId: String?) : Event()
        class AddPetButtonClicked(
            val petType: String,
            val avatar: String,
            val breed: String,
            val name: String,
            val birthday: LocalDate,
            val gender: String,
            val chipNumber: String,
            val isSterilized: Boolean
        ) : Event()

        object NavigateBack : Event()
        class NavigateToAvatarEdit(val petId: String, val petType: PetType) : Event()
    }

    data class State(
        val petType: PetType? = null,
        val avatar: String? = null,
        val breeds: List<String> = listOf(),
        val pet: Pet? = null,
        val isLoading: Boolean = false
    ) : ViewState

    sealed class Effect : ViewSideEffect {
        class NavigateBack(val confirmed: Boolean = false) : Navigation()

        sealed class Navigation : Effect(), NavigationSideEffect {
            class ToAvatarEdit(val petId: String, val petType: PetType) : Navigation()
            class ToPetSetup(val petId: String) : Navigation()
        }
    }
}