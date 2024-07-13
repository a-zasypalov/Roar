package com.gaoyun.roar.presentation.user_edit

import com.gaoyun.roar.domain.user.EditUserUseCase
import com.gaoyun.roar.domain.user.GetCurrentUserUseCase
import com.gaoyun.roar.model.domain.User
import com.gaoyun.roar.presentation.MultiplatformBaseViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class EditUserScreenViewModel(
    private val getUser: GetCurrentUserUseCase,
    private val editUserUseCase: EditUserUseCase,
) : MultiplatformBaseViewModel<EditUserScreenContract.Event, EditUserScreenContract.State, EditUserScreenContract.Effect>() {

    init {
        buildScreenState()
    }

    override fun setInitialState() = EditUserScreenContract.State(isLoading = true)

    override fun handleEvents(event: EditUserScreenContract.Event) {
        when (event) {
            is EditUserScreenContract.Event.OnSaveAccountClick -> saveUserProfile(event.user)
            is EditUserScreenContract.Event.NavigateBack -> setEffect { EditUserScreenContract.Effect.NavigateBack }
        }
    }

    private fun buildScreenState() = scope.launch {
        getUser.getCurrentUser()
            .catch { it.printStackTrace() }
            .collect { setState { copy(isLoading = false, userToEdit = it) } }
    }

    private fun saveUserProfile(user: User) = scope.launch {
        editUserUseCase.update(user).collect {
            setEffect { EditUserScreenContract.Effect.NavigateBack }
        }
    }

}