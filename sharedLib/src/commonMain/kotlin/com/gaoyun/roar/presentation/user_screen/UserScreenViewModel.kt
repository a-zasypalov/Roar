package com.gaoyun.roar.presentation.user_screen

import com.gaoyun.roar.domain.AppPreferencesUseCase
import com.gaoyun.roar.domain.backup.CreateBackupUseCase
import com.gaoyun.roar.domain.backup.ImportBackupUseCase
import com.gaoyun.roar.domain.pet.GetPetUseCase
import com.gaoyun.roar.domain.user.GetCurrentUserUseCase
import com.gaoyun.roar.domain.user.LogoutUseCase
import com.gaoyun.roar.network.SynchronisationApi
import com.gaoyun.roar.presentation.BaseViewModel
import com.gaoyun.roar.util.ColorTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class UserScreenViewModel(
    private val getUser: GetCurrentUserUseCase,
    private val createBackupUseCase: CreateBackupUseCase,
    private val importBackupUseCase: ImportBackupUseCase,
    private val appPreferencesUseCase: AppPreferencesUseCase,
    private val synchronisationApi: SynchronisationApi,
    private val logoutUseCase: LogoutUseCase,
    private val getPetUseCase: GetPetUseCase,
) : BaseViewModel<UserScreenContract.Event, UserScreenContract.State, UserScreenContract.Effect>() {

    val backupState = MutableStateFlow("")

    override fun setInitialState() = UserScreenContract.State(isLoading = true)

    override fun handleEvents(event: UserScreenContract.Event) {
        when (event) {
            is UserScreenContract.Event.OnDeleteAccountClick -> {}
            is UserScreenContract.Event.OnLogout -> logout()
            is UserScreenContract.Event.OnEditAccountClick -> setEffect { UserScreenContract.Effect.Navigation.ToUserEdit }
            is UserScreenContract.Event.OnCreateBackupClick -> createBackup()
            is UserScreenContract.Event.OnUseBackup -> useBackup(event.backup, event.removeOld)
            is UserScreenContract.Event.OnDynamicColorsStateChange -> setDynamicColor(event.active)
            is UserScreenContract.Event.OnStaticColorThemePick -> staticThemeChange(event.theme)
            is UserScreenContract.Event.OnNumberOfRemindersOnMainScreen -> setNumberOfRemindersOnMainScreen(event.newNumber)
            is UserScreenContract.Event.OnHomeScreenModeChange -> switchHomeScreenMode()
            is UserScreenContract.Event.OnAboutScreenClick -> setEffect { UserScreenContract.Effect.Navigation.ToAboutScreen }
            is UserScreenContract.Event.NavigateBack -> {
                setEffect { UserScreenContract.Effect.NavigateBack }
            }
        }
    }

    fun buildScreenState() = scope.launch {
        getUser.getCurrentUser()
            .catch {
                it.printStackTrace()
            }
            .collect { user ->
                val numberOfPets = getPetUseCase.getPetByUserId(user.id).firstOrNull()?.size ?: 1
                setState {
                    copy(
                        isLoading = false,
                        user = user,
                        activeColorTheme = appPreferencesUseCase.staticTheme()?.let { ColorTheme.valueOf(it) } ?: ColorTheme.Orange,
                        dynamicColorActive = appPreferencesUseCase.dynamicColorsIsActive(),
                        screenModeFull = appPreferencesUseCase.homeScreenModeFull(),
                        numberOfRemindersOnMainScreenState = appPreferencesUseCase.numberOfRemindersOnMainScreen().toString(),
                        numberOfPets = numberOfPets
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

    private fun useBackup(backup: ByteArray, removeOld: Boolean) = scope.launch {
        importBackupUseCase.importBackup(backup, removeOld).collect {
            setEffect { UserScreenContract.Effect.BackupApplied }
        }
    }

    private fun setDynamicColor(active: Boolean) {
        appPreferencesUseCase.setDynamicColors(active)
        setState { copy(dynamicColorActive = active) }
    }

    private fun staticThemeChange(theme: ColorTheme) {
        appPreferencesUseCase.setStaticTheme(theme.name)
        setState { copy(activeColorTheme = theme) }
    }

    private fun switchHomeScreenMode() {
        appPreferencesUseCase.switchHomeScreenMode()
        setState { copy(screenModeFull = !screenModeFull) }
    }

    private fun setNumberOfRemindersOnMainScreen(number: Int) {
        appPreferencesUseCase.setNumberOfRemindersOnMainScreen(number)
        setState { copy(numberOfRemindersOnMainScreenState = number.toString()) }
    }

    private fun logout() = scope.launch {
        setState { copy(isLoading = true) }
        createBackupUseCase.createBackupToSync()
            .catch { it.printStackTrace() }
            .map { if (it != null) synchronisationApi.sendBackup(it) }
            .first()
        logoutUseCase.logout().firstOrNull()
        setEffect { UserScreenContract.Effect.LoggedOut }
    }

}