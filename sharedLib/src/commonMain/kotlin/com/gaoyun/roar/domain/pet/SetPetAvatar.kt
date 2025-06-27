package com.gaoyun.roar.domain.pet

import com.gaoyun.roar.repository.PetRepository

class SetPetAvatar(private val repository: PetRepository) {

    fun setAvatar(petId: String, avatar: String) {
        repository.getPet(petId)?.let { pet ->
            repository.insertPet(pet.copy(avatar = avatar))
        }
    }
}