package com.gaoyun.roar.presentation.add_pet

import com.gaoyun.roar.domain.pet.AddPetUseCase
import com.gaoyun.roar.presentation.BaseViewModel
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class AddPetScreenViewModel :
    BaseViewModel<AddPetScreenContract.Event, AddPetScreenContract.State, AddPetScreenContract.Effect>(),
    KoinComponent {

    private val addPetUseCase: AddPetUseCase by inject()

    override fun setInitialState() = AddPetScreenContract.State(false)

    override fun handleEvents(event: AddPetScreenContract.Event) {}

    fun addPet(
        petType: String,
        breed: String,
        name: String,
        birthday: LocalDate,
        isSterilized: Boolean
    ) = scope.launch {
        addPetUseCase.addPet(petType, breed, name, birthday, isSterilized).collect {
            petAddedSuccessful()
        }
    }

    private fun petAddedSuccessful() {
        setEffect { AddPetScreenContract.Effect.PetAdded }
    }
}