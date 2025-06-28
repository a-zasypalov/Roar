package com.gaoyun.roar.presentation.user_edit

import androidx.lifecycle.viewModelScope
import com.gaoyun.roar.domain.user.EditUserUseCase
import com.gaoyun.roar.domain.user.GetCurrentUserUseCase
import com.gaoyun.roar.model.domain.User
import com.gaoyun.roar.presentation.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class EditUserScreenState(
    val isLoading: Boolean = false,
    val userToEdit: User? = null
)

class EditUserScreenViewModel(
    private val getUser: GetCurrentUserUseCase,
    private val editUserUseCase: EditUserUseCase,
) : BaseViewModel() {

    override val viewState = MutableStateFlow(EditUserScreenState(isLoading = true))

    init {
        loadUser()
    }

    private fun loadUser() = viewModelScope.launch {
        getUser.getCurrentUser()
            .collect { user ->
                viewState.update { it.copy(userToEdit = user, isLoading = false) }
            }
    }

    fun saveUser(user: User, onComplete: () -> Unit) = viewModelScope.launch {
        editUserUseCase.update(user).collect {
            onComplete()
        }
    }
}