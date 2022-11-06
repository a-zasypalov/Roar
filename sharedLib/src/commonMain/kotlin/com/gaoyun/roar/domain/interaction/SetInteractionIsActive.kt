package com.gaoyun.roar.domain.interaction

import com.gaoyun.roar.repository.InteractionRepository
import kotlinx.coroutines.flow.flow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class SetInteractionIsActive : KoinComponent {

    private val repository: InteractionRepository by inject()

    fun activateInteraction(id: String) = flow { emit(repository.setInteractionIsActive(id, true)) }

    fun deactivateInteraction(id: String) = flow { emit(repository.setInteractionIsActive(id, false)) }


}