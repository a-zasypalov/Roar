package com.gaoyun.roar.domain.pet

import com.gaoyun.roar.domain.user.GetCurrentUserUseCase
import com.gaoyun.roar.model.domain.Pet
import com.gaoyun.roar.repository.PetRepository
import com.gaoyun.roar.util.toLocalDate
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class AddPetUseCase : KoinComponent {

    private val repository: PetRepository by inject()
    private val getUserUseCase: GetCurrentUserUseCase by inject()

    suspend fun addPet(
        petType: String,
        breed: String,
        name: String,
        birthday: LocalDate,
        isSterilized: Boolean
    ) {
        val userId = getUserUseCase.getCurrentUser().id
        repository.insertPet(
            Pet(
                petType = petType,
                breed = breed,
                name = name,
                birthday = birthday,
                isSterilized = isSterilized,
                userId = userId,
                dateCreated = Clock.System.now().toLocalDate(),
            )
        )
    }

}