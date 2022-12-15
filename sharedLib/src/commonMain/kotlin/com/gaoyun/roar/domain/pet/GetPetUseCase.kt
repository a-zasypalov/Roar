package com.gaoyun.roar.domain.pet

import com.gaoyun.roar.repository.PetRepository
import com.gaoyun.roar.util.NoPetException
import kotlinx.coroutines.flow.flow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.coroutines.cancellation.CancellationException

class GetPetUseCase : KoinComponent {

    private val repository: PetRepository by inject()

    @Throws(NoPetException::class, CancellationException::class)
    fun getPet(petId: String) = flow {
        emit(repository.getPet(petId) ?: throw NoPetException())
    }

    fun getPets(petIds: List<String>) = flow {
        emit(petIds.mapNotNull { petId -> repository.getPet(petId) })
    }

    fun getPetByUserId(userId: String) = flow {
        emit(repository.getPetsByUser(userId))
    }

}