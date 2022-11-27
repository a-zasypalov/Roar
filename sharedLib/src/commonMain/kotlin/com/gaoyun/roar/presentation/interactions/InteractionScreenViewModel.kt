package com.gaoyun.roar.presentation.interactions

import com.gaoyun.roar.domain.interaction.GetInteraction
import com.gaoyun.roar.domain.interaction.InsertInteraction
import com.gaoyun.roar.domain.pet.GetPetUseCase
import com.gaoyun.roar.model.domain.interactions.withoutReminders
import com.gaoyun.roar.presentation.BaseViewModel
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class InteractionScreenViewModel :
    BaseViewModel<InteractionScreenContract.Event, InteractionScreenContract.State, InteractionScreenContract.Effect>(),
    KoinComponent {

    private val getPetUseCase: GetPetUseCase by inject()
    private val getInteraction: GetInteraction by inject()
    private val saveInteraction: InsertInteraction by inject()

    override fun setInitialState() = InteractionScreenContract.State(isLoading = true)

    override fun handleEvents(event: InteractionScreenContract.Event) {
        when (event) {
            is InteractionScreenContract.Event.OnSaveNotes -> saveNoteState(event.notes)
            is InteractionScreenContract.Event.OnEditButtonClick -> {}
        }
    }

    fun buildScreenState(interactionId: String) = scope.launch {
        getInteraction.getInteractionWithReminders(interactionId).collect { interaction ->
            getPetUseCase.getPet(interaction.petId).collect { pet ->
                setState { copy(pet = pet, isLoading = false, interaction = interaction) }
            }
        }
    }

    private fun saveNoteState(notes: String) = scope.launch {
        viewState.value.interaction?.let {
            saveInteraction.insertInteraction(it.copy(notes = notes.trim()).withoutReminders()).firstOrNull()
        }
    }
}