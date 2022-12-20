package com.gaoyun.roar.presentation.user_screen

import com.gaoyun.roar.domain.user.GetCurrentUserUseCase
import com.gaoyun.roar.presentation.BaseViewModel
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class UserScreenViewModel : BaseViewModel<UserScreenContract.Event, UserScreenContract.State, UserScreenContract.Effect>(),
    KoinComponent {

    private val getUser: GetCurrentUserUseCase by inject()

    override fun setInitialState() = UserScreenContract.State(isLoading = true)

    override fun handleEvents(event: UserScreenContract.Event) {
        when (event) {
            is UserScreenContract.Event.OnDeleteAccountClick -> {}
        }
    }

    fun buildScreenState() = scope.launch {
        try {
            val user = getUser.getCurrentUser()
            setState { copy(isLoading = false, user = user) }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}