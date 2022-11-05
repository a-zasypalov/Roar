package com.gaoyun.roar.presentation.add_reminder

import com.gaoyun.roar.domain.interaction.GetInteractionTemplatesForPetType
import com.gaoyun.roar.domain.pet.AddPetUseCase
import com.gaoyun.roar.domain.pet.GetPetUseCase
import com.gaoyun.roar.model.domain.Pet
import com.gaoyun.roar.model.domain.PetType
import com.gaoyun.roar.model.domain.User
import com.gaoyun.roar.presentation.BaseViewModel
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class AddReminderScreenViewModel :
    BaseViewModel<AddReminderScreenContract.Event, AddReminderScreenContract.State, AddReminderScreenContract.Effect>(),
    KoinComponent {

    private val getInteractionTemplatesUseCase: GetInteractionTemplatesForPetType by inject()
    private val getPetUseCase: GetPetUseCase by inject()

    override fun setInitialState() = AddReminderScreenContract.State(isLoading = true)

    override fun handleEvents(event: AddReminderScreenContract.Event) {
        when (event) {
            is AddReminderScreenContract.Event.AddReminderButtonClicked -> with(event) {

            }
        }
    }

    fun buildScreenState(petId: String) = scope.launch {
        val pet = getPetUseCase.getPet(petId)
        getInteractionTemplates(pet)
    }

    private suspend fun getInteractionTemplates(pet: Pet) {
        getInteractionTemplatesUseCase.getInteractionTemplatesForPetType(pet.petType)
            .collect { templates ->
                setState { copy(isLoading = false, pet = pet, templates = templates) }
            }
    }
}