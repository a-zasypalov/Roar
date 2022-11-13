package com.gaoyun.roar.presentation.add_reminder.choose_template

import com.gaoyun.roar.domain.interaction_template.GetInteractionTemplatesForPetType
import com.gaoyun.roar.domain.pet.GetPetUseCase
import com.gaoyun.roar.model.domain.Pet
import com.gaoyun.roar.presentation.BaseViewModel
import kotlinx.coroutines.launch
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
            is AddReminderScreenContract.Event.TemplateChosen -> with(event) {
                setEffect { AddReminderScreenContract.Effect.TemplateChosen(templateId = templateId, petId = petId) }
            }
        }
    }

    fun buildScreenState(petId: String) = scope.launch {
        getPetUseCase.getPet(petId).collect { pet ->
            getInteractionTemplates(pet)
        }
    }

    private suspend fun getInteractionTemplates(pet: Pet) {
        getInteractionTemplatesUseCase.getInteractionTemplatesForPetType(pet.petType)
            .collect { templates ->
                setState { copy(isLoading = false, pet = pet, templates = templates) }
            }
    }
}