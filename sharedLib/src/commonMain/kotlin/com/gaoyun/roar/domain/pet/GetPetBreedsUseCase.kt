package com.gaoyun.roar.domain.pet

import com.gaoyun.roar.model.domain.PetType
import com.gaoyun.roar.repository.PetRepository
import kotlinx.coroutines.flow.flow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class GetPetBreedsUseCase : KoinComponent {

    private val repository: PetRepository by inject()

    fun getBreeds(petType: PetType) = flow {
        emit(repository.getBreeds(petType.toString()))
    }

}