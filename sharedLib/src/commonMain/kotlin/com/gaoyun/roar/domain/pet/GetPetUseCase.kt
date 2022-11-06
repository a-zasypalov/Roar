package com.gaoyun.roar.domain.pet

import com.gaoyun.roar.model.domain.Pet
import com.gaoyun.roar.repository.PetRepository
import com.gaoyun.roar.util.NoPetException
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.coroutines.cancellation.CancellationException

class GetPetUseCase : KoinComponent {

    private val repository: PetRepository by inject()

    @Throws(NoPetException::class, CancellationException::class)
    fun getPet(petId: String): Pet {
        return repository.getPet(petId) ?: throw NoPetException()
    }

    fun getPets(petIds: List<String>): List<Pet> {
        return petIds.mapNotNull { petId -> repository.getPet(petId) }
    }

    fun getPetByUserId(userId: String): List<Pet> {
        return repository.getPetsByUser(userId)
    }

}