package com.gaoyun.roar.presentation.add_reminder.setup_reminder

import com.gaoyun.roar.domain.interaction_template.GetInteractionTemplate
import com.gaoyun.roar.domain.pet.GetPetUseCase
import com.gaoyun.roar.model.domain.Pet
import com.gaoyun.roar.model.domain.interactions.toInteractionRepeatConfig
import com.gaoyun.roar.presentation.BaseViewModel
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class SetupReminderScreenViewModel :
    BaseViewModel<SetupReminderScreenContract.Event, SetupReminderScreenContract.State, SetupReminderScreenContract.Effect>(),
    KoinComponent {

    private val getInteractionTemplateUseCase: GetInteractionTemplate by inject()
    private val getPetUseCase: GetPetUseCase by inject()

    override fun setInitialState() = SetupReminderScreenContract.State(isLoading = true)

    override fun handleEvents(event: SetupReminderScreenContract.Event) {
        when (event) {
            is SetupReminderScreenContract.Event.RepeatConfigChanged -> with(event) {
                setState { copy(repeatConfig = config.toInteractionRepeatConfig()) }
            }
            is SetupReminderScreenContract.Event.OnSaveButtonClick -> with(event) {

            }
        }
    }

    fun buildScreenState(petId: String, templateId: String) = scope.launch {
        getPetUseCase.getPet(petId).collect { pet ->
            getInteractionTemplate(pet, templateId)
        }
    }

    private suspend fun getInteractionTemplate(pet: Pet, templateId: String) {
        getInteractionTemplateUseCase.getInteractionTemplate(templateId, pet.petType)
            .collect { template ->
                setState { copy(isLoading = false, pet = pet, template = template) }
            }
    }
}