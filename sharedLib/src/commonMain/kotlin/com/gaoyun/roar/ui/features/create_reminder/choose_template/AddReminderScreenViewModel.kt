package com.gaoyun.roar.presentation.add_reminder.choose_template

import androidx.lifecycle.viewModelScope
import com.gaoyun.roar.domain.interaction.GetInteraction
import com.gaoyun.roar.domain.interaction_template.GetInteractionTemplatesForPetType
import com.gaoyun.roar.domain.pet.GetPetUseCase
import com.gaoyun.roar.model.domain.PetWithInteractions
import com.gaoyun.roar.model.domain.interactions.InteractionTemplate
import com.gaoyun.roar.model.domain.withInteractions
import com.gaoyun.roar.ui.common.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AddReminderScreenViewModel(
    private val getInteractionTemplatesUseCase: GetInteractionTemplatesForPetType,
    private val getPetUseCase: GetPetUseCase,
    private val getInteraction: GetInteraction,
) : BaseViewModel() {

    override val viewState = MutableStateFlow(AddReminderScreenContractState(isLoading = true))

    fun initialize(petId: String) = viewModelScope.launch {
        getPetUseCase.getPet(petId)
            .filterNotNull()
            .collect { pet ->
                val interactions = getInteraction.getInteractionByPet(pet.id).firstOrNull()
                val petWithInteractions = pet.withInteractions(interactions)
                getInteractionTemplatesUseCase.getInteractionTemplatesForPetType(pet.petType)
                    .collect { templates ->
                        viewState.update {
                            it.copy(
                                isLoading = false,
                                pet = petWithInteractions,
                                templates = templates
                            )
                        }
                    }
            }
    }
}

data class AddReminderScreenContractState(
    val isLoading: Boolean = false,
    val pet: PetWithInteractions? = null,
    val templates: List<InteractionTemplate> = emptyList()
)