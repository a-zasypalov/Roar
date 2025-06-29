package com.gaoyun.roar.ui.features.home

import androidx.lifecycle.viewModelScope
import com.gaoyun.roar.domain.AppPreferencesUseCase
import com.gaoyun.roar.domain.interaction.InteractionsListBuilder
import com.gaoyun.roar.domain.pet.RemovePetUseCase
import com.gaoyun.roar.domain.reminder.SetReminderComplete
import com.gaoyun.roar.domain.user.CheckUserExistingUseCase
import com.gaoyun.roar.domain.user.GetCurrentUserUseCase
import com.gaoyun.roar.domain.user.LogoutUseCase
import com.gaoyun.roar.domain.user.RegisterUserUseCase
import com.gaoyun.roar.model.domain.PetWithInteractions
import com.gaoyun.roar.model.domain.User
import com.gaoyun.roar.model.domain.interactions.InteractionWithReminders
import com.gaoyun.roar.model.domain.withInteractions
import com.gaoyun.roar.network.SynchronisationApi
import com.gaoyun.roar.ui.common.BaseViewModel
import com.gaoyun.roar.ui.features.registration.RegistrationLauncher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDateTime

class HomeScreenViewModel(
    private val checkUserExistingUseCase: CheckUserExistingUseCase,
    private val getUserUseCase: GetCurrentUserUseCase,
    private val removePet: RemovePetUseCase,
    private val setReminderComplete: SetReminderComplete,
    private val appPreferencesUseCase: AppPreferencesUseCase,
    private val synchronisationApi: SynchronisationApi,
    private val registerUserUseCase: RegisterUserUseCase,
    private val interactionsListBuilder: InteractionsListBuilder,
    private val logoutUseCase: LogoutUseCase,
    val registrationLauncher: RegistrationLauncher,
) : BaseViewModel() {

    override val viewState = MutableStateFlow(
        HomeScreenContractState(isLoading = true)
    )

    fun checkUserRegistered(onRegistrationRequired: () -> Unit) = viewModelScope.launch {
        if (checkUserExistingUseCase.isUserExisting().not()) {
            onRegistrationRequired()
        } else {
            loadUser(onRegistrationRequired)
        }
    }

    fun onLoginUser(id: String, onRegistrationRequired: () -> Unit) = viewModelScope.launch {
        registerUserUseCase.login(id)
        synchronisationApi.retrieveBackup(
            onFinish = { viewModelScope.launch { loadUser(onRegistrationRequired) } },
            onAuthException = { logout(onRegistrationRequired) }
        )
    }

    private suspend fun loadUser(onRegistrationRequired: () -> Unit) {
        getUserUseCase.getCurrentUser()
            .onEach { user ->
                user?.let { safeUser ->
                    synchronisationApi.retrieveBackup(
                        onFinish = { loadPets(safeUser) },
                        onAuthException = { logout(onRegistrationRequired) }
                    )
                } ?: onRegistrationRequired()
            }
            .filterNotNull()
            .collect { loadPets(it) }
    }

    private fun logout(onRegistrationRequired: () -> Unit) = viewModelScope.launch {
        logoutUseCase.logout().firstOrNull()
        onRegistrationRequired()
    }

    private fun loadPets(user: User) = viewModelScope.launch {
        val screenModeFull = appPreferencesUseCase.homeScreenModeFull()
        val showCustomizationPrompt = appPreferencesUseCase.showCustomizationPrompt()

        val pets = interactionsListBuilder.buildPetState(user.id, screenModeFull)
        val inactive = pets.takeIf { it.size == 1 }?.firstOrNull()?.id?.let {
            interactionsListBuilder.buildInactiveInteractionsListFor(it)
        } ?: emptyList()

        viewState.update {
            it.copy(
                user = user,
                pets = pets,
                inactiveInteractions = inactive,
                isLoading = false,
                screenModeFull = screenModeFull,
                showCustomizationPrompt = showCustomizationPrompt
            )
        }
    }

    fun setPetChooserShow(show: Boolean) {
        viewState.update { it.copy(showPetChooser = show) }
    }

    fun onDeletePetClicked() {
        viewState.update { it.copy(deletePetDialogShow = true) }
    }

    fun hideDeletePetDialog() {
        viewState.update { it.copy(deletePetDialogShow = false) }
    }

    fun onDeletePetConfirmed(
        pet: PetWithInteractions,
        onPetDeleted: () -> Unit
    ) = viewModelScope.launch {
        hideDeletePetDialog()
        delay(250)
        removePet.removePet(pet.id).collect {
            checkUserRegistered(onPetDeleted)
        }
    }

    fun markReminderComplete(
        pet: PetWithInteractions,
        reminderId: String,
        completed: Boolean,
        completionDateTime: LocalDateTime
    ) = viewModelScope.launch {
        setReminderComplete.setComplete(reminderId, completed, completionDateTime)
            .filterNotNull()
            .collect { interaction ->
                val newPets = viewState.value.pets.map { petItem ->
                    if (petItem.id != pet.id) petItem
                    else petItem.withInteractions(
                        interactionsListBuilder.buildListOnCompletingReminder(
                            petItem.interactions,
                            interaction,
                            completed
                        )
                    )
                }
                viewState.update { it.copy(pets = newPets) }
            }
    }

    fun closeCustomizationPrompt() {
        appPreferencesUseCase.closeCustomizationPrompt()
        viewState.update { it.copy(showCustomizationPrompt = false) }
    }
}

data class HomeScreenContractState(
    val user: User? = null,
    val pets: List<PetWithInteractions> = emptyList(),
    val inactiveInteractions: List<InteractionWithReminders> = emptyList(),
    val isLoading: Boolean = false,
    val showPetChooser: Boolean = false,
    val deletePetDialogShow: Boolean = false,
    val screenModeFull: Boolean = true,
    val showCustomizationPrompt: Boolean = false,
)