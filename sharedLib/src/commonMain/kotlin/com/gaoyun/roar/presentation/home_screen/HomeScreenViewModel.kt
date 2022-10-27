package com.gaoyun.roar.presentation.home_screen

import com.gaoyun.roar.domain.user.CheckUserExistingUseCase
import com.gaoyun.roar.domain.user.GetCurrentUserUseCase
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

    override fun setInitialState() = HomeScreenContract.State(null, true)

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
            setUserDataState(getUserUseCase.getCurrentUser())
        } catch (noUser: NoUserException) {
            noUser.printStackTrace()
            setNoUserState()
        }
    }

    private fun setNoUserState() = setState { copy(user = null, isLoading = false) }
    private fun setUserDataState(user: User) = setState { copy(user = user, isLoading = false) }

    fun openRegistration() {
        setEffect { HomeScreenContract.Effect.Navigation.ToUserRegistration }
    }

    fun openAddPetScreen() {
        setEffect { HomeScreenContract.Effect.Navigation.ToAddPet }
    }
}