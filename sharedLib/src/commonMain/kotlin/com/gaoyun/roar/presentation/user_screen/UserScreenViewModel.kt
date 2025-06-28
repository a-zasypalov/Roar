package com.gaoyun.roar.presentation.user_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gaoyun.roar.domain.AppPreferencesUseCase
import com.gaoyun.roar.domain.backup.CreateBackupUseCase
import com.gaoyun.roar.domain.backup.ImportBackupUseCase
import com.gaoyun.roar.domain.pet.GetPetUseCase
import com.gaoyun.roar.domain.user.DeleteAccountUseCase
import com.gaoyun.roar.domain.user.GetCurrentUserUseCase
import com.gaoyun.roar.domain.user.LogoutUseCase
import com.gaoyun.roar.model.domain.User
import com.gaoyun.roar.network.SynchronisationApi
import com.gaoyun.roar.util.AppIcon
import com.gaoyun.roar.util.BackupHandler
import com.gaoyun.roar.util.ColorTheme
import com.gaoyun.roar.util.ThemeChanger
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
data class UserScreenState @OptIn(ExperimentalTime::class) constructor(
    val isLoading: Boolean = false,
    val dynamicColorActive: Boolean = false,
    val screenModeFull: Boolean = true,
    val activeColorTheme: ColorTheme = ColorTheme.Orange,
    val numberOfRemindersOnMainScreenState: String = "2",
    val numberOfPets: Int = 1,
    val lastSync: LocalDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
    val user: User? = null,
)

class UserScreenViewModel(
    private val getUser: GetCurrentUserUseCase,
    private val createBackupUseCase: CreateBackupUseCase,
    private val importBackupUseCase: ImportBackupUseCase,
    private val appPreferencesUseCase: AppPreferencesUseCase,
    private val synchronisationApi: SynchronisationApi,
    private val logoutUseCase: LogoutUseCase,
    private val deleteAccountUseCase: DeleteAccountUseCase,
    private val getPetUseCase: GetPetUseCase,
    private val themeChanger: ThemeChanger,
    private val backupHandler: BackupHandler
) : ViewModel() {

    val viewState = MutableStateFlow(UserScreenState(isLoading = true))
    private val snackbarMessages = MutableSharedFlow<String>()
    val messages = snackbarMessages.asSharedFlow()

    @OptIn(ExperimentalTime::class)
    fun loadScreen() = viewModelScope.launch {
        getUser.getCurrentUser()
            .filterNotNull()
            .collect { user ->
                val pets = getPetUseCase.getPetByUserId(user.id).firstOrNull()?.size ?: 1
                viewState.update {
                    it.copy(
                        isLoading = false,
                        user = user,
                        activeColorTheme = appPreferencesUseCase.staticTheme()?.let { c -> ColorTheme.valueOf(c) }
                            ?: ColorTheme.Orange,
                        dynamicColorActive = appPreferencesUseCase.dynamicColorsIsActive(),
                        screenModeFull = appPreferencesUseCase.homeScreenModeFull(),
                        numberOfRemindersOnMainScreenState = appPreferencesUseCase.numberOfRemindersOnMainScreen().toString(),
                        lastSync = Instant.fromEpochMilliseconds(appPreferencesUseCase.getLastSync())
                            .toLocalDateTime(TimeZone.currentSystemDefault()),
                        numberOfPets = pets
                    )
                }
            }
    }

    fun createBackup(onComplete: () -> Unit) = viewModelScope.launch {
        createBackupUseCase.createBackup().collect { backup ->
            backupHandler.exportBackup(backup) {
                viewModelScope.launch {
                    snackbarMessages.emit("Backup saved successfully")
                    onComplete()
                }
            }
        }
    }

    fun applyBackup(backup: ByteArray, removeOld: Boolean, onComplete: () -> Unit) = viewModelScope.launch {
        importBackupUseCase.importBackup(backup, removeOld).collect {
            snackbarMessages.emit("Backup applied successfully")
            onComplete()
        }
    }

    fun logout(onComplete: () -> Unit) = viewModelScope.launch {
        viewState.update { it.copy(isLoading = true) }
        createBackupUseCase.createBackupToSync()
            .catch { it.printStackTrace() }
            .map { if (it != null) synchronisationApi.sendBackup(it) }
            .firstOrNull()
        logoutUseCase.logout().firstOrNull()
        onComplete()
    }

    fun deleteAccount(onComplete: () -> Unit) = viewModelScope.launch {
        viewState.update { it.copy(isLoading = true) }
        deleteAccountUseCase.deleteAccount().firstOrNull()
        onComplete()
    }

    fun toggleDynamicColor(active: Boolean) {
        appPreferencesUseCase.setDynamicColors(active)
        themeChanger.applyTheme()
        viewState.update { it.copy(dynamicColorActive = active) }
    }

    fun changeStaticTheme(theme: ColorTheme) {
        appPreferencesUseCase.setStaticTheme(theme.name)
        themeChanger.applyTheme()
        viewState.update { it.copy(activeColorTheme = theme) }
    }

    fun switchHomeScreenMode() {
        appPreferencesUseCase.switchHomeScreenMode()
        viewState.update { it.copy(screenModeFull = !it.screenModeFull) }
    }

    fun setNumberOfRemindersOnMainScreen(number: Int) {
        appPreferencesUseCase.setNumberOfRemindersOnMainScreen(number)
        viewState.update { it.copy(numberOfRemindersOnMainScreenState = number.toString()) }
    }

    fun changeAppIcon(icon: AppIcon) {
        themeChanger.activateIcon(icon)
    }

    fun triggerBackupImport(onComplete: () -> Unit) {
        backupHandler.importBackup { imported ->
            applyBackup(imported.backup, imported.removeOld) {
                onComplete()
            }
        }
    }
}