package com.gaoyun.roar.presentation.home_screen

import com.gaoyun.roar.domain.AppPreferencesUseCase
import com.gaoyun.roar.domain.interaction.InteractionsListBuilder
import com.gaoyun.roar.domain.pet.RemovePetUseCase
import com.gaoyun.roar.domain.reminder.SetReminderComplete
import com.gaoyun.roar.domain.user.CheckUserExistingUseCase
import com.gaoyun.roar.domain.user.GetCurrentUserUseCase
import com.gaoyun.roar.domain.user.RegisterUserUseCase
import com.gaoyun.roar.model.domain.PetWithInteractions
import com.gaoyun.roar.model.domain.User
import com.gaoyun.roar.model.domain.withInteractions
import com.gaoyun.roar.network.SynchronisationApi
import com.gaoyun.roar.presentation.BaseViewModel
import com.gaoyun.roar.util.NoUserException
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.onEach
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
    private val syncApi: SynchronisationApi,
) : BaseViewModel<HomeScreenContract.Event, HomeScreenContract.State, HomeScreenContract.Effect>() {

    override fun setInitialState() = HomeScreenContract.State(null, emptyList(), listOf(), true)

    override fun handleEvents(event: HomeScreenContract.Event) {
        when (event) {
            is HomeScreenContract.Event.ToUserScreenClicked -> setEffect { HomeScreenContract.Effect.Navigation.ToUserScreen }
            is HomeScreenContract.Event.ToEditPetClicked -> setEffect { HomeScreenContract.Effect.Navigation.ToEditPet(event.pet) }
            is HomeScreenContract.Event.LoginUser -> loginUser(event.id)
            is HomeScreenContract.Event.SetPetChooserShow -> setDialogShow(event.show)
            is HomeScreenContract.Event.PetChosenForReminderCreation -> {
                setDialogShow(false)
                setEffect { HomeScreenContract.Effect.Navigation.ToAddReminder(event.petId) }
            }

            is HomeScreenContract.Event.InteractionClicked -> setEffect {
                HomeScreenContract.Effect.Navigation.ToInteractionDetails(event.interactionId)
            }

            is HomeScreenContract.Event.OnDeletePetClicked -> setState { copy(deletePetDialogShow = true) }
            is HomeScreenContract.Event.OnDeletePetConfirmed -> scope.launch {
                hideDeletePetDialog()
                delay(250)
                removePet.removePet(event.pet.id).collect { checkUserRegistered() }
            }

            is HomeScreenContract.Event.OnInteractionCheckClicked -> setReminderComplete(event.pet, event.reminderId, event.completed, event.completionDateTime)
        }
    }

    fun checkUserRegistered() = scope.launch {
        if (checkUserExistingUseCase.isUserExisting().not()) {
            openRegistration()
        } else {
            getUser()
        }
    }

    private fun loginUser(id: String) = scope.launch {
        registerUserUseCase.login(id)
        syncApi.retrieveBackup { scope.launch { getUser() } }
    }

    private suspend fun getUser() {
        getUserUseCase.getCurrentUser()
            .catch {
                it.printStackTrace()
                if (it is NoUserException) {
                    openRegistration()
                }
            }
            .onEach { synchronisationApi.retrieveBackup() }
            .collect { getPets(it) }
    }

    private fun getPets(user: User) = scope.launch {
        val screenModeFull = appPreferencesUseCase.homeScreenModeFull()

        interactionsListBuilder.buildPetState(user.id, screenModeFull).takeIf { it.isNotEmpty() }?.let { pets ->
            val inactiveInteractions = pets.takeIf { it.size == 1 }?.firstOrNull()?.id?.let { interactionsListBuilder.buildInactiveInteractionsListFor(it) } ?: listOf()
            setState { copy(user = user, pets = pets, inactiveInteractions = inactiveInteractions, isLoading = false, screenModeFull = screenModeFull) }
        } ?: setState { copy(user = user, pets = emptyList(), isLoading = false, screenModeFull = screenModeFull) }
    }

    private fun setReminderComplete(pet: PetWithInteractions, reminderId: String, isComplete: Boolean, completionDateTime: LocalDateTime) = scope.launch {
        setReminderComplete.setComplete(reminderId, isComplete, completionDateTime).filterNotNull().collect { interaction ->
            val newPets = viewState.value.pets.toMutableList().map { petItem ->
                if (petItem.id != pet.id) return@map petItem
                return@map petItem.withInteractions(interactionsListBuilder.buildListOnCompletingReminder(petItem.interactions, interaction, isComplete))
            }
            setState { copy(pets = newPets) }
        }
    }

    private fun setDialogShow(show: Boolean) = setState { copy(showPetChooser = show) }
    fun hideDeletePetDialog() = setState { copy(deletePetDialogShow = false) }
    fun openRegistration() = setEffect { HomeScreenContract.Effect.Navigation.ToUserRegistration }
    fun openAddPetScreen() = setEffect { HomeScreenContract.Effect.Navigation.ToAddPet }
    fun openPetScreen(petId: String) = setEffect { HomeScreenContract.Effect.Navigation.ToPetScreen(petId) }
}