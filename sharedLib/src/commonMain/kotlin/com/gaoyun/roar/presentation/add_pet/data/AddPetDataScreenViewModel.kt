package com.gaoyun.roar.presentation.add_pet.data

import com.gaoyun.roar.domain.pet.AddPetUseCase
import com.gaoyun.roar.domain.pet.GetPetBreedsUseCase
import com.gaoyun.roar.model.domain.PetType
import com.gaoyun.roar.model.domain.toGender
import com.gaoyun.roar.model.domain.toPetType
import com.gaoyun.roar.presentation.BaseViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class AddPetDataScreenViewModel :
    BaseViewModel<AddPetDataScreenContract.Event, AddPetDataScreenContract.State, AddPetDataScreenContract.Effect>(),
    KoinComponent {

    private val addPetUseCase: AddPetUseCase by inject()
    private val petBreedsUseCase: GetPetBreedsUseCase by inject()

    override fun setInitialState() = AddPetDataScreenContract.State(isLoading = false)

    override fun handleEvents(event: AddPetDataScreenContract.Event) {
        when (event) {
            is AddPetDataScreenContract.Event.AddPetButtonClicked -> with(event) {
                addPet(
                    petType = petType,
                    breed = breed,
                    name = name,
                    avatar = avatar,
                    birthday = birthday,
                    isSterilized = isSterilized,
                    chipNumber = chipNumber,
                    gender = gender
                )
            }
            is AddPetDataScreenContract.Event.PetDataInit -> {
                setState { copy(petType = event.petType.toPetType(), avatar = event.avatar) }
                getBreeds(event.petType.toPetType())
            }
        }
    }

    private fun getBreeds(petType: PetType) = scope.launch {
        petBreedsUseCase.getBreeds(petType).collect {
            setState { copy(breeds = it) }
        }
    }

    private fun addPet(
        petType: String,
        breed: String,
        name: String,
        avatar: String,
        birthday: LocalDate,
        gender: String,
        chipNumber: String,
        isSterilized: Boolean
    ) = scope.launch {
        addPetUseCase.addPet(
            petType = petType,
            breed = breed,
            name = name,
            avatar = avatar,
            birthday = birthday,
            chipNumber = chipNumber,
            isSterilized = isSterilized,
            gender = gender.toGender()
        ).catch { it.printStackTrace() }
            .collectLatest { petId ->
                petAddedSuccessful(petId)
            }
    }

    private fun petAddedSuccessful(petId: String) {
        setEffect { AddPetDataScreenContract.Effect.Navigation.ToPetSetup(petId) }
    }
}