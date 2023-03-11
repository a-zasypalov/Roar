package com.gaoyun.roar.presentation.user_screen

import com.gaoyun.roar.domain.DynamicColorsUseCase
import com.gaoyun.roar.domain.backup.CreateBackupUseCase
import com.gaoyun.roar.domain.backup.ImportBackupUseCase
import com.gaoyun.roar.domain.user.GetCurrentUserUseCase
import com.gaoyun.roar.presentation.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class UserScreenViewModel : BaseViewModel<UserScreenContract.Event, UserScreenContract.State, UserScreenContract.Effect>(),
    KoinComponent {

    private val getUser: GetCurrentUserUseCase by inject()
    private val createBackupUseCase: CreateBackupUseCase by inject()
    private val importBackupUseCase: ImportBackupUseCase by inject()
    private val dynamicColorsUseCase: DynamicColorsUseCase by inject()

    val backupState = MutableStateFlow("")

    override fun setInitialState() = UserScreenContract.State(isLoading = true)

    override fun handleEvents(event: UserScreenContract.Event) {
        when (event) {
            is UserScreenContract.Event.OnDeleteAccountClick -> {}
            is UserScreenContract.Event.OnEditAccountClick -> {}
            is UserScreenContract.Event.OnCreateBackupClick -> createBackup()
            is UserScreenContract.Event.OnUseBackup -> useBackup(event.backupString, event.removeOld)
            is UserScreenContract.Event.OnDynamicColorsStateChange -> setDynamicColor(event.active)
        }
    }

    fun buildScreenState() = scope.launch {
        getUser.getCurrentUser()
            .catch {
                it.printStackTrace()
            }
            .collect { user ->
                setState {
                    copy(
                        isLoading = false,
                        user = user,
                        dynamicColorActive = dynamicColorsUseCase.dynamicColorsIsActive()
                    )
                }
            }
    }

    private fun createBackup() = scope.launch {
        createBackupUseCase.createBackup().collect {
            backupState.value = it
            setEffect { UserScreenContract.Effect.BackupReady }
        }
    }

    private fun useBackup(backupString: String, removeOld: Boolean) = scope.launch {
        importBackupUseCase.importBackup(backupString, removeOld).collect {
            setEffect { UserScreenContract.Effect.BackupApplied }
        }
    }

    private fun setDynamicColor(active: Boolean) {
        dynamicColorsUseCase.setDynamicColors(active)
        setState { copy(dynamicColorActive = active) }
    }

}