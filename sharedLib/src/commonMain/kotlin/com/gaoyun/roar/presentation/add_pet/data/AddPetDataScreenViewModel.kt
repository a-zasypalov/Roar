package com.gaoyun.roar.presentation.add_pet.data

import androidx.lifecycle.viewModelScope
import com.gaoyun.roar.domain.pet.AddPetUseCase
import com.gaoyun.roar.domain.pet.GetPetBreedsUseCase
import com.gaoyun.roar.domain.pet.GetPetUseCase
import com.gaoyun.roar.domain.pet.SetPetAvatar
import com.gaoyun.roar.model.domain.LanguageCode
import com.gaoyun.roar.model.domain.Pet
import com.gaoyun.roar.model.domain.PetType
import com.gaoyun.roar.model.domain.toGender
import com.gaoyun.roar.model.domain.toLanguageCode
import com.gaoyun.roar.model.domain.toPetType
import com.gaoyun.roar.presentation.BaseViewModel
import com.gaoyun.roar.ui.navigation.NavigationSideEffect
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate

class AddPetDataScreenViewModel(
    private val addPetUseCase: AddPetUseCase,
    private val petBreedsUseCase: GetPetBreedsUseCase,
    private val getPet: GetPetUseCase,
    private val setPetAvatar: SetPetAvatar,
) : BaseViewModel() {

    override val viewState = MutableStateFlow(AddPetDataScreenContractState(isLoading = true))

    fun initialize(
        petType: String,
        avatar: String,
        petId: String?,
        localeCode: String,
        noBreedString: String
    ) {
        viewState.update { it.copy(petType = petType.toPetType(), avatar = avatar) }
        getPetInfo(petType.toPetType(), petId, localeCode.toLanguageCode(), noBreedString)
    }

    private fun getPetInfo(
        petType: PetType,
        petId: String?,
        languageCode: LanguageCode,
        noBreedString: String
    ) = viewModelScope.launch {
        val pet = petId?.let { id -> getPet.getPet(id).firstOrNull() }
        petBreedsUseCase.getBreeds(petType, languageCode).collect { breeds ->
            val listWithNone = breeds.toMutableList().apply { add(0, noBreedString) }
            viewState.update { it.copy(breeds = listWithNone, pet = pet, isLoading = false) }
        }
    }

    fun addOrEditPet(
        petType: String,
        breed: String,
        name: String,
        avatar: String,
        birthday: LocalDate,
        gender: String,
        chipNumber: String,
        isSterilized: Boolean,
        onSuccessNavigate: (String) -> Unit,
        onBack: () -> Unit
    ) = viewModelScope.launch {
        val existingPet = viewState.value.pet
        if (existingPet != null) {
            addPetUseCase.addPet(
                existingPet.copy(
                    name = name,
                    breed = breed,
                    avatar = avatar,
                    birthday = birthday,
                    gender = gender.toGender(),
                    chipNumber = chipNumber,
                    isSterilized = isSterilized
                )
            ).catch { it.printStackTrace() }
                .collectLatest {
                    onBack()
                }
        } else {
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
                .collectLatest { newId ->
                    onSuccessNavigate(newId)
                }
        }
    }

    fun revertPetAvatar(
        petId: String,
        avatar: String,
        onBack: () -> Unit
    ) = viewModelScope.launch {
        setPetAvatar.setAvatar(petId, avatar)
        onBack()
    }
}

class ToAvatarEdit(val petId: String, val petType: PetType) : NavigationSideEffect
class ToPetSetup(val petId: String) : NavigationSideEffect

data class AddPetDataScreenContractState(
    val petType: PetType? = null,
    val avatar: String? = null,
    val breeds: List<String> = listOf(),
    val pet: Pet? = null,
    val isLoading: Boolean = false
)