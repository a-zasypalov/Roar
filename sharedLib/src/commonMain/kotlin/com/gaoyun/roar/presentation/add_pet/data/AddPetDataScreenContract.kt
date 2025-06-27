package com.gaoyun.roar.presentation.add_pet.data

import com.gaoyun.roar.model.domain.PetType
import kotlinx.datetime.LocalDate

class AddPetDataScreenContract {
    sealed class Event {
        class PetDataInit(val petType: String, val avatar: String, val petId: String?, val localeCode: String, val noBreedString: String) : Event()
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

        data object NavigateBack : Event()
        class NavigateToAvatarEdit(val petId: String, val petType: PetType) : Event()
    }
}