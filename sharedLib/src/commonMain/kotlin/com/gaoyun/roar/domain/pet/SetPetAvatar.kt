package com.gaoyun.roar.domain.pet

import com.gaoyun.roar.repository.PetRepository
import kotlinx.coroutines.flow.flow

class SetPetAvatar(private val repository: PetRepository) {

    fun setAvatar(petId: String, avatar: String) = flow {
        repository.getPet(petId)?.let { pet ->
            repository.insertPet(pet.copy(avatar = avatar))
        }
        emit(petId)
    }

}