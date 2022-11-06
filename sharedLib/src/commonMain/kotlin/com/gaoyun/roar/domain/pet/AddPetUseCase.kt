package com.gaoyun.roar.domain.pet

import com.gaoyun.roar.domain.user.GetCurrentUserUseCase
import com.gaoyun.roar.model.domain.Pet
import com.gaoyun.roar.model.domain.toPetType
import com.gaoyun.roar.repository.PetRepository
import com.gaoyun.roar.util.toLocalDate
import kotlinx.coroutines.flow.flow
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class AddPetUseCase : KoinComponent {

    private val repository: PetRepository by inject()
    private val getUserUseCase: GetCurrentUserUseCase by inject()

    fun addPet(
        petType: String,
        breed: String,
        name: String,
        avatar: String,
        birthday: LocalDate,
        isSterilized: Boolean
    ) = flow {
        val userId = getUserUseCase.getCurrentUser().id
        val pet = Pet(
            petType = petType.toPetType(),
            breed = breed,
            name = name,
            avatar = avatar,
            birthday = birthday,
            isSterilized = isSterilized,
            userId = userId,
            dateCreated = Clock.System.now().toLocalDate(),
        )

        repository.insertPet(pet)

        emit(pet.id)
    }

}