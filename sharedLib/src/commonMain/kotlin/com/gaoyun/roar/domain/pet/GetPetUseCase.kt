package com.gaoyun.roar.domain.pet

import com.gaoyun.roar.model.domain.Pet
import com.gaoyun.roar.repository.PetRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetPetUseCase(private val repository: PetRepository) {

    fun getPet(petId: String): Flow<Pet?> = flow {
        emit(repository.getPet(petId))
    }

    fun getPets(petIds: List<String>) = flow {
        emit(petIds.mapNotNull { petId -> repository.getPet(petId) })
    }

    fun getPetByUserId(userId: String) = flow {
        emit(repository.getPetsByUser(userId))
    }

    fun getPetByUserIdForBackup(userId: String) = flow {
        emit(repository.getPetsByUser(userId))
    }

}