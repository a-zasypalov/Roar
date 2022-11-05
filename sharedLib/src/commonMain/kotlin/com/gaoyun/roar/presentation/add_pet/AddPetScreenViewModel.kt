package com.gaoyun.roar.presentation.add_pet

import com.gaoyun.roar.domain.pet.AddPetUseCase
import com.gaoyun.roar.domain.pet.GetPetBreedsUseCase
import com.gaoyun.roar.model.domain.PetType
import com.gaoyun.roar.model.domain.toPetType
import com.gaoyun.roar.presentation.BaseViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class AddPetScreenViewModel :
    BaseViewModel<AddPetScreenContract.Event, AddPetScreenContract.State, AddPetScreenContract.Effect>(),
    KoinComponent {

    private val addPetUseCase: AddPetUseCase by inject()
    private val petBreedsUseCase: GetPetBreedsUseCase by inject()

    override fun setInitialState() = AddPetScreenContract.State(isLoading = false)

    override fun handleEvents(event: AddPetScreenContract.Event) {
        when (event) {
            is AddPetScreenContract.Event.AddPetButtonClicked -> with(event) {
                addPet(petType, breed, name, birthday, isSterilized)
            }
            is AddPetScreenContract.Event.PetTypeChosen -> getBreeds(event.petType.toPetType())
        }
    }

    private fun getBreeds(petType: PetType) = scope.launch {
        petBreedsUseCase.getBreeds(petType).collect {
            setState { copy(petType = petType, breeds = it) }
        }
    }

    private fun addPet(
        petType: String,
        breed: String,
        name: String,
        birthday: LocalDate,
        isSterilized: Boolean
    ) = scope.launch {
        addPetUseCase.addPet(petType, breed, name, birthday, isSterilized)
            .catch { it.printStackTrace() }
            .collect {
                petAddedSuccessful()
            }
    }

    private fun petAddedSuccessful() {
        setEffect { AddPetScreenContract.Effect.PetAdded }
    }
}