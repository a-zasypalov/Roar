package com.gaoyun.roar.domain.pet

import com.gaoyun.roar.model.domain.LanguageCode
import com.gaoyun.roar.model.domain.PetType
import com.gaoyun.roar.repository.PetRepository
import kotlinx.coroutines.flow.flow

class GetPetBreedsUseCase(private val repository: PetRepository) {

    fun getBreeds(petType: PetType, languageCode: LanguageCode) = flow {
        emit(repository.getBreeds(petType.toString(), languageCode).sorted())
    }

}