package com.gaoyun.roar.presentation.user_edit

import com.gaoyun.roar.domain.user.EditUserUseCase
import com.gaoyun.roar.domain.user.GetCurrentUserUseCase
import com.gaoyun.roar.model.domain.User
import com.gaoyun.roar.presentation.BaseViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class EditUserScreenViewModel : BaseViewModel<EditUserScreenContract.Event, EditUserScreenContract.State, EditUserScreenContract.Effect>(),
    KoinComponent {

    private val getUser: GetCurrentUserUseCase by inject()
    private val editUserUseCase: EditUserUseCase by inject()

    override fun setInitialState() = EditUserScreenContract.State(isLoading = true)

    override fun handleEvents(event: EditUserScreenContract.Event) {
        when (event) {
            is EditUserScreenContract.Event.OnSaveAccountClick -> saveUserProfile(event.user)
        }
    }

    fun buildScreenState() = scope.launch {
        getUser.getCurrentUser()
            .catch { it.printStackTrace() }
            .collect { setState { copy(isLoading = false, userToEdit = it) } }
    }

    private fun saveUserProfile(user: User) = scope.launch {
        editUserUseCase.update(user).collect {
            setEffect { EditUserScreenContract.Effect.Navigation.NavigateBack }
        }
    }

}