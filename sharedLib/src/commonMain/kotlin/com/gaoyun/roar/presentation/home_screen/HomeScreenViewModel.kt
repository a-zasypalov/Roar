package com.gaoyun.roar.presentation.home_screen

import com.gaoyun.roar.domain.pet.GetPetUseCase
import com.gaoyun.roar.domain.user.CheckUserExistingUseCase
import com.gaoyun.roar.domain.user.GetCurrentUserUseCase
import com.gaoyun.roar.model.domain.Pet
import com.gaoyun.roar.model.domain.User
import com.gaoyun.roar.presentation.BaseViewModel
import com.gaoyun.roar.util.NoUserException
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class HomeScreenViewModel :
    BaseViewModel<HomeScreenContract.Event, HomeScreenContract.State, HomeScreenContract.Effect>(),
    KoinComponent {

    private val checkUserExistingUseCase: CheckUserExistingUseCase by inject()
    private val getUserUseCase: GetCurrentUserUseCase by inject()
    private val getPetUseCase: GetPetUseCase by inject()

    override fun setInitialState() = HomeScreenContract.State(null, emptyList(), true)

    override fun handleEvents(event: HomeScreenContract.Event) {

    }

    fun checkUserRegistered() = scope.launch {
        if (checkUserExistingUseCase.isUserExisting().not()) {
            setNoUserState()
        } else {
            getUser()
        }
    }

    private suspend fun getUser() {
        try {
            val user = getUserUseCase.getCurrentUser()
            getPets(user)
        } catch (noUser: NoUserException) {
            noUser.printStackTrace()
            setNoUserState()
        }
    }

    private fun getPets(user: User) {
        val pets = getPetUseCase.getPetByUserId(user.id)
        if (pets.isNotEmpty()) {
            setPetsState(user, pets)
        } else {
            setUserDataState(user)
        }
    }

    private fun setNoUserState() = setState { copy(user = null, isLoading = false) }
    private fun setUserDataState(user: User) = setState { copy(user = user, isLoading = false) }
    private fun setPetsState(user: User, pets: List<Pet>) = setState { copy(user = user, pets = pets, isLoading = false) }

    fun openRegistration() {
        setEffect { HomeScreenContract.Effect.Navigation.ToUserRegistration }
    }

    fun openAddPetScreen() {
        setEffect { HomeScreenContract.Effect.Navigation.ToAddPet }
    }

    fun openAddReminderScreen(pet: Pet) {
        setEffect { HomeScreenContract.Effect.Navigation.ToAddReminder(pet) }
    }
}