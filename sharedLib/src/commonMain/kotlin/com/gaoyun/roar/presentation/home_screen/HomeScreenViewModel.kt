package com.gaoyun.roar.presentation.home_screen

import com.gaoyun.roar.domain.interaction.GetInteraction
import com.gaoyun.roar.domain.pet.GetPetUseCase
import com.gaoyun.roar.domain.pet.RemovePetUseCase
import com.gaoyun.roar.domain.reminder.SetReminderComplete
import com.gaoyun.roar.domain.user.CheckUserExistingUseCase
import com.gaoyun.roar.domain.user.GetCurrentUserUseCase
import com.gaoyun.roar.model.domain.PetWithInteractions
import com.gaoyun.roar.model.domain.User
import com.gaoyun.roar.model.domain.withInteractions
import com.gaoyun.roar.model.domain.withoutInteractions
import com.gaoyun.roar.presentation.BaseViewModel
import com.gaoyun.roar.util.NoUserException
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class HomeScreenViewModel :
    BaseViewModel<HomeScreenContract.Event, HomeScreenContract.State, HomeScreenContract.Effect>(),
    KoinComponent {

    private val checkUserExistingUseCase: CheckUserExistingUseCase by inject()
    private val getUserUseCase: GetCurrentUserUseCase by inject()
    private val getPetUseCase: GetPetUseCase by inject()
    private val getInteractions: GetInteraction by inject()
    private val removePet: RemovePetUseCase by inject()
    private val setReminderComplete: SetReminderComplete by inject()

    override fun setInitialState() = HomeScreenContract.State(null, emptyList(), true)

    override fun handleEvents(event: HomeScreenContract.Event) {
        when (event) {
            is HomeScreenContract.Event.SetPetChooserShow -> setDialogShow(event.show)
            is HomeScreenContract.Event.PetChosenForReminderCreation -> {
                scope.launch {
                    setDialogShow(false)
                    delay(250)
                    openAddReminderScreen(event.petId)
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
            is HomeScreenContract.Event.OnInteractionCheckClicked -> setReminderComplete(event.pet, event.reminderId, event.completed)
        }
    }

    fun checkUserRegistered() = scope.launch {
        if (checkUserExistingUseCase.isUserExisting().not()) {
            setNoUserState()
        } else {
            getUser()
        }
    }

    private fun getUser() {
        try {
            val user = getUserUseCase.getCurrentUser()
            getPets(user)
        } catch (noUser: NoUserException) {
            noUser.printStackTrace()
            setNoUserState()
        }
    }

    private fun getPets(user: User) = scope.launch {
        val pets = (getPetUseCase.getPetByUserId(user.id).firstOrNull() ?: emptyList())
            .map { pet ->
                pet.withInteractions(
                    interactions = getInteractions.getInteractionByPet(pet.id).firstOrNull() ?: emptyList()
                )
            }

        if (pets.isNotEmpty()) {
            setPetsState(user, pets)
        } else {
            setUserDataState(user)
        }
    }

    private fun setReminderComplete(pet: PetWithInteractions, reminderId: String, isComplete: Boolean) = scope.launch {
        setReminderComplete.setComplete(reminderId, isComplete).collect {
            it?.let { interaction ->
                setState {
                    copy(
                        pets = viewState.value.pets.toMutableList().map { petItem ->
                            if (petItem.id == pet.id) {
                                val newInteractions = petItem.interactions.toMutableList().apply {
                                    removeAll { item -> item.id == interaction.id }
                                    add(interaction)
                                }
                                petItem.withoutInteractions().withInteractions(newInteractions)
                            } else petItem
                        }, showLastReminder = showLastReminder || isComplete
                    )
                }
            }
        }
    }

    private fun setNoUserState() = setState { copy(user = null, isLoading = false) }
    private fun setUserDataState(user: User) = setState { copy(user = user, isLoading = false) }
    private fun setPetsState(user: User, pets: List<PetWithInteractions>) = setState { copy(user = user, pets = pets, isLoading = false) }
    private fun setDialogShow(show: Boolean) = setState { copy(showPetChooser = show) }
    private fun hideDeletePetDialog() = setState { copy(deletePetDialogShow = false) }
    fun openRegistration() = setEffect { HomeScreenContract.Effect.Navigation.ToUserRegistration }
    fun openAddPetScreen() = setEffect { HomeScreenContract.Effect.Navigation.ToAddPet }
    private fun openAddReminderScreen(petId: String) = setEffect { HomeScreenContract.Effect.Navigation.ToAddReminder(petId) }
    fun openPetScreen(petId: String) = setEffect { HomeScreenContract.Effect.Navigation.ToPetScreen(petId) }
}