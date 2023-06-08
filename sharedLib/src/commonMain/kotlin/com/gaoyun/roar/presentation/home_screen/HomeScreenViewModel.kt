package com.gaoyun.roar.presentation.home_screen

import com.gaoyun.roar.domain.AppPreferencesUseCase
import com.gaoyun.roar.domain.interaction.GetInteraction
import com.gaoyun.roar.domain.pet.GetPetUseCase
import com.gaoyun.roar.domain.pet.RemovePetUseCase
import com.gaoyun.roar.domain.reminder.SetReminderComplete
import com.gaoyun.roar.domain.user.CheckUserExistingUseCase
import com.gaoyun.roar.domain.user.GetCurrentUserUseCase
import com.gaoyun.roar.domain.user.RegisterUserUseCase
import com.gaoyun.roar.model.domain.PetWithInteractions
import com.gaoyun.roar.model.domain.User
import com.gaoyun.roar.model.domain.interactions.withReminders
import com.gaoyun.roar.model.domain.interactions.withoutReminders
import com.gaoyun.roar.model.domain.withInteractions
import com.gaoyun.roar.model.domain.withoutInteractions
import com.gaoyun.roar.network.SynchronisationApi
import com.gaoyun.roar.presentation.BaseViewModel
import com.gaoyun.roar.util.NoUserException
import com.gaoyun.roar.util.SharedDateUtils.MAX_DATE
import com.gaoyun.roar.util.SharedDateUtils.MIN_DATE
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDateTime

class HomeScreenViewModel(
    private val checkUserExistingUseCase: CheckUserExistingUseCase,
    private val getUserUseCase: GetCurrentUserUseCase,
    private val getPetUseCase: GetPetUseCase,
    private val getInteractions: GetInteraction,
    private val removePet: RemovePetUseCase,
    private val setReminderComplete: SetReminderComplete,
    private val appPreferencesUseCase: AppPreferencesUseCase,
    private val synchronisationApi: SynchronisationApi,
    private val registerUserUseCase: RegisterUserUseCase,
    private val syncApi: SynchronisationApi,
) : BaseViewModel<HomeScreenContract.Event, HomeScreenContract.State, HomeScreenContract.Effect>() {

    override fun setInitialState() = HomeScreenContract.State(null, emptyList(), true)

    override fun handleEvents(event: HomeScreenContract.Event) {
        when (event) {
            is HomeScreenContract.Event.ToUserScreenClicked -> setEffect { HomeScreenContract.Effect.Navigation.ToUserScreen }
            is HomeScreenContract.Event.ToEditPetClicked -> setEffect { HomeScreenContract.Effect.Navigation.ToEditPet(event.pet) }
            is HomeScreenContract.Event.LoginUser -> loginUser(event.id)
            is HomeScreenContract.Event.SetPetChooserShow -> setDialogShow(event.show)
            is HomeScreenContract.Event.PetChosenForReminderCreation -> {
                scope.launch {
                    setDialogShow(false)
                    setEffect { HomeScreenContract.Effect.Navigation.ToAddReminder(event.petId) }
                }
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

    private suspend fun getUser() = scope.launch {
        getUserUseCase.getCurrentUser()
            .catch {
                it.printStackTrace()
                if (it is NoUserException) {
                    openRegistration()
                }
            }
            .map { user ->
                synchronisationApi.retrieveBackup()
                user
            }
            .collect { getPets(it) }
    }

    private fun getPets(user: User) = scope.launch {
        val screenModeFull = appPreferencesUseCase.homeScreenModeFull()
        val remindersPerPet = appPreferencesUseCase.numberOfRemindersOnMainScreen()
        val pets = (getPetUseCase.getPetByUserId(user.id).firstOrNull() ?: emptyList())

        val petsState = if (pets.size > 1 || screenModeFull.not()) {
            pets.map { pet ->
                val interactions = getInteractions.getInteractionByPet(pet.id).firstOrNull() ?: emptyList()
                val reminders = interactions
                    .flatMap { it.reminders }
                    .filter { !it.isCompleted }
                    .sortedBy { it.dateTime }
                    .take(remindersPerPet)

                val interactionsToShow = interactions.map { it.withoutReminders().withReminders(reminders.filter { r -> r.interactionId == it.id }) }
                    .filter { it.reminders.isNotEmpty() }
                    .sortedBy { it.reminders.minOfOrNull { r -> r.dateTime } ?: MAX_DATE }
                    .groupBy { it.group }

                pet.withInteractions(interactionsToShow)
            }.sortedBy { pet ->
                pet.interactions.values.flatten()
                    .minOfOrNull { interaction -> interaction.reminders.minOfOrNull { reminder -> reminder.dateTime } ?: MAX_DATE } ?: MIN_DATE
            }
        } else {
            pets.map { pet ->
                val interactionsToShow = (getInteractions.getInteractionByPet(pet.id).firstOrNull() ?: emptyList())
                    .sortedBy { it.reminders.filter { r -> r.isCompleted.not() }.minOfOrNull { r -> r.dateTime } }
                    .groupBy { it.group }

                pet.withInteractions(interactionsToShow)
            }
        }

        if (petsState.isNotEmpty()) {
            setState { copy(user = user, pets = petsState, isLoading = false, screenModeFull = screenModeFull) }
        } else {
            setState { copy(user = user, pets = emptyList(), isLoading = false, screenModeFull = screenModeFull) }
        }
    }

    private fun setReminderComplete(pet: PetWithInteractions, reminderId: String, isComplete: Boolean, completionDateTime: LocalDateTime) = scope.launch {
        setReminderComplete.setComplete(reminderId, isComplete, completionDateTime).filterNotNull().collect { interaction ->
            val newPets = viewState.value.pets.toMutableList().map { petItem ->
                if (petItem.id == pet.id) {
                    val newInteractions = petItem.interactions.toMutableMap()
                    val newList = newInteractions[interaction.group]?.toMutableList()?.apply {
                        removeAll { item -> item.id == interaction.id }
                        add(interaction)
                    }
                    newInteractions[interaction.group] = newList ?: emptyList()
                    return@map petItem.withoutInteractions().withInteractions(newInteractions)
                } else {
                    return@map petItem
                }
            }
            setState { copy(pets = newPets, showLastReminder = showLastReminder || isComplete) }
        }
    }

    private fun setDialogShow(show: Boolean) = setState { copy(showPetChooser = show) }
    fun hideDeletePetDialog() = setState { copy(deletePetDialogShow = false) }
    fun openRegistration() = setEffect { HomeScreenContract.Effect.Navigation.ToUserRegistration }
    fun openAddPetScreen() = setEffect { HomeScreenContract.Effect.Navigation.ToAddPet }
    fun openPetScreen(petId: String) = setEffect { HomeScreenContract.Effect.Navigation.ToPetScreen(petId) }
}