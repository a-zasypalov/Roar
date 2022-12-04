package com.gaoyun.roar.domain.pet

import com.gaoyun.roar.repository.PetRepository
import kotlinx.coroutines.flow.flow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class RemovePetUseCase : KoinComponent {

    private val repository: PetRepository by inject()

    fun removePet(petId: String) = flow {
        repository.deletePet(petId)
        emit(Unit)
    }

}