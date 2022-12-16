package com.gaoyun.roar.domain.pet

import com.gaoyun.roar.repository.PetRepository
import kotlinx.coroutines.flow.flow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class SetPetAvatar : KoinComponent {

    private val repository: PetRepository by inject()

    fun setAvatar(petId: String, avatar: String) = flow {
        repository.getPet(petId)?.let { pet ->
            repository.insertPet(pet.copy(avatar = avatar))
        }
        emit(petId)
    }

}