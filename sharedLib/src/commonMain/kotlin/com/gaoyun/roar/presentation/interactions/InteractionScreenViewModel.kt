package com.gaoyun.roar.presentation.interactions

import com.gaoyun.roar.domain.interaction.GetInteraction
import com.gaoyun.roar.domain.pet.GetPetUseCase
import com.gaoyun.roar.presentation.BaseViewModel
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class InteractionScreenViewModel :
    BaseViewModel<InteractionScreenContract.Event, InteractionScreenContract.State, InteractionScreenContract.Effect>(),
    KoinComponent {

    private val getPetUseCase: GetPetUseCase by inject()
    private val getInteraction: GetInteraction by inject()

    override fun setInitialState() = InteractionScreenContract.State(isLoading = true)

    override fun handleEvents(event: InteractionScreenContract.Event) {
//        when (event) {
//            is InteractionScreenContract.Event.AddReminderButtonClicked -> setEffect {
//                InteractionScreenContract.Effect.Navigation.ToInteractionTemplates(event.petId)
//            }
//        }
    }

    fun buildScreenState(interactionId: String) = scope.launch {
        getInteraction.getInteractionWithReminders(interactionId).collect { interaction ->
            getPetUseCase.getPet(interaction.petId).collect { pet ->
                setState { copy(pet = pet, isLoading = false, interaction = interaction) }
            }
        }
    }
}