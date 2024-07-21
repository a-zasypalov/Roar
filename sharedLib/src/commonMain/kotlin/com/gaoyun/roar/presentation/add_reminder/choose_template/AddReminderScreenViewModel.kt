package com.gaoyun.roar.presentation.add_reminder.choose_template

import com.gaoyun.roar.domain.interaction.GetInteraction
import com.gaoyun.roar.domain.interaction_template.GetInteractionTemplatesForPetType
import com.gaoyun.roar.domain.pet.GetPetUseCase
import com.gaoyun.roar.model.domain.PetWithInteractions
import com.gaoyun.roar.model.domain.withInteractions
import com.gaoyun.roar.presentation.MultiplatformBaseViewModel
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class AddReminderScreenViewModel(
    private val getInteractionTemplatesUseCase: GetInteractionTemplatesForPetType,
    private val getPetUseCase: GetPetUseCase,
    private val getInteraction: GetInteraction,
) : MultiplatformBaseViewModel<AddReminderScreenContract.Event, AddReminderScreenContract.State, AddReminderScreenContract.Effect>() {

    override fun setInitialState() = AddReminderScreenContract.State(isLoading = true)

    override fun handleEvents(event: AddReminderScreenContract.Event) {
        when (event) {
            is AddReminderScreenContract.Event.TemplateChosen -> with(event) {
                setEffect { AddReminderScreenContract.Effect.Navigation.ToReminderSetup(templateId = templateId, petId = petId) }
            }
        }
    }

    fun buildScreenState(petId: String) = scope.launch {
        getPetUseCase.getPet(petId).filterNotNull().collect { pet ->
            getInteractionTemplates(pet.withInteractions(getInteraction.getInteractionByPet(pet.id).firstOrNull()))
        }
    }

    private suspend fun getInteractionTemplates(pet: PetWithInteractions) {
        getInteractionTemplatesUseCase.getInteractionTemplatesForPetType(pet.petType)
            .collect { templates ->
                setState { copy(isLoading = false, pet = pet, templates = templates) }
            }
    }
}