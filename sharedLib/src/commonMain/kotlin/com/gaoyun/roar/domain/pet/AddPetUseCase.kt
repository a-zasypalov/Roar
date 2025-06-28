package com.gaoyun.roar.domain.pet

import com.gaoyun.roar.domain.user.GetCurrentUserUseCase
import com.gaoyun.roar.model.domain.Gender
import com.gaoyun.roar.model.domain.Pet
import com.gaoyun.roar.model.domain.toPetType
import com.gaoyun.roar.repository.PetRepository
import com.gaoyun.roar.util.toLocalDate
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.datetime.LocalDate
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

class AddPetUseCase(
    private val repository: PetRepository,
    private val getUserUseCase: GetCurrentUserUseCase,
) {

    @OptIn(ExperimentalTime::class)
    fun addPet(
        petType: String,
        breed: String,
        name: String,
        avatar: String,
        birthday: LocalDate,
        gender: Gender,
        chipNumber: String,
        isSterilized: Boolean
    ) = flow {
        val userId = getUserUseCase.getCurrentUser().firstOrNull()?.id ?: return@flow
        val pet = Pet(
            petType = petType.toPetType(),
            breed = breed,
            name = name,
            avatar = avatar,
            birthday = birthday,
            isSterilized = isSterilized,
            userId = userId,
            gender = gender,
            chipNumber = chipNumber,
            dateCreated = Clock.System.now().toLocalDate(),
        )

        repository.insertPet(pet)

        emit(pet.id)
    }

    fun addPet(pet: Pet) = flow {
        repository.insertPet(pet)
        emit(pet.id)
    }

}