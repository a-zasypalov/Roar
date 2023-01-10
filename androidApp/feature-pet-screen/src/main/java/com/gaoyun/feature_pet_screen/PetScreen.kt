package com.gaoyun.feature_pet_screen

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavHostController
import com.gaoyun.common.NavigationKeys
import com.gaoyun.common.OnLifecycleEvent
import com.gaoyun.common.theme.RoarTheme
import com.gaoyun.common.ui.*
import com.gaoyun.feature_pet_screen.view.PetContainer
import com.gaoyun.roar.model.domain.*
import com.gaoyun.roar.model.domain.interactions.InteractionGroup
import com.gaoyun.roar.model.domain.interactions.InteractionType
import com.gaoyun.roar.model.domain.interactions.InteractionWithReminders
import com.gaoyun.roar.presentation.LAUNCH_LISTEN_FOR_EFFECTS
import com.gaoyun.roar.presentation.pet_screen.PetScreenContract
import com.gaoyun.roar.presentation.pet_screen.PetScreenViewModel
import com.gaoyun.roar.util.toLocalDate
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.datetime.*
import org.koin.androidx.compose.getViewModel
import kotlin.time.Duration.Companion.hours

@Composable
fun PetScreenDestination(
    navHostController: NavHostController,
    petId: String,
) {
    val viewModel: PetScreenViewModel = getViewModel()
    val state = viewModel.viewState.collectAsState().value

    OnLifecycleEvent { _, event ->
        if (event == Lifecycle.Event.ON_CREATE) {
            viewModel.buildScreenState(petId)
        }
    }

    PetScreen(
        state = state,
        effectFlow = viewModel.effect,
        onEventSent = { event -> viewModel.setEvent(event) },
        onNavigationRequested = { navigationEffect ->
            when (navigationEffect) {
                is PetScreenContract.Effect.Navigation.ToInteractionDetails ->
                    navHostController.navigate("${NavigationKeys.Route.INTERACTION_DETAIL}/${navigationEffect.interactionId}")
                is PetScreenContract.Effect.Navigation.ToInteractionTemplates ->
                    navHostController.navigate("${NavigationKeys.Route.ADD_REMINDER}/${navigationEffect.petId}")
                is PetScreenContract.Effect.Navigation.ToEditPet ->
                    navHostController.navigate("${NavigationKeys.Route.EDIT}/${NavigationKeys.Route.PET_DETAIL}/${navigationEffect.pet.id}/${navigationEffect.pet.avatar}/${navigationEffect.pet.petType}")
                is PetScreenContract.Effect.Navigation.NavigateBack ->
                    navHostController.navigateUp()
            }
        },
        viewModel = viewModel
    )

}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun PetScreen(
    state: PetScreenContract.State,
    effectFlow: Flow<PetScreenContract.Effect>,
    onEventSent: (event: PetScreenContract.Event) -> Unit,
    onNavigationRequested: (navigationEffect: PetScreenContract.Effect.Navigation) -> Unit,
    viewModel: PetScreenViewModel
) {

    LaunchedEffect(LAUNCH_LISTEN_FOR_EFFECTS) {
        effectFlow.onEach { effect ->
            when (effect) {
                is PetScreenContract.Effect.Navigation -> onNavigationRequested(effect)
                else -> {}
            }
        }.collect()
    }

    SurfaceScaffold(
        floatingActionButton = {
            RoarExtendedFloatingActionButton(
                icon = Icons.Filled.Add,
                contentDescription = "Add reminder",
                text = "Reminder",
                onClick = { onEventSent(PetScreenContract.Event.AddReminderButtonClicked(state.pet?.id ?: "")) })
        },
        floatingActionButtonPosition = FabPosition.End
    ) {
        if (state.deletePetDialogShow) {
            RemovePetConfirmationDialog(
                petName = state.pet?.name.toString(),
                onDismiss = viewModel::hideDeletePetDialog,
                onConfirm = { onEventSent(PetScreenContract.Event.OnDeletePetConfirmed(state.pet?.id ?: "")) }
            )
        }

        BoxWithLoader(isLoading = state.isLoading) {
            state.pet?.let { pet ->
                PetContainer(
                    pet = pet.withInteractions(state.interactions),
                    showLastReminder = state.showLastReminder,
                    onInteractionClick = { onEventSent(PetScreenContract.Event.InteractionClicked(it)) },
                    onDeletePetClick = { onEventSent(PetScreenContract.Event.OnDeletePetClicked) },
                    onEditPetClick = { onNavigationRequested(PetScreenContract.Effect.Navigation.ToEditPet(pet = pet)) },
                    onInteractionCheckClicked = { reminderId, completed, completionDateTime ->
                        onEventSent(
                            PetScreenContract.Event.OnInteractionCheckClicked(
                                reminderId,
                                completed,
                                completionDateTime.toKotlinLocalDateTime()
                            )
                        )
                    },
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                        .padding(start = 8.dp, end = 8.dp)
                        .fillMaxWidth()
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun PetScreenPreview() {
    RoarTheme {
        SurfaceScaffold {
            PetContainer(
                Pet(
                    petType = PetType.CAT,
                    breed = "Colorpoint Shorthair",
                    name = "Senior Android Developer",
                    avatar = "ic_cat_15",
                    userId = "123",
                    birthday = Clock.System.now().toLocalDate(),
                    isSterilized = false,
                    gender = Gender.MALE,
                    chipNumber = "123123456456",
                    dateCreated = Clock.System.now().toLocalDate()
                ).withInteractions(
                    listOf(
                        InteractionWithReminders(
                            petId = "",
                            type = InteractionType.CUSTOM,
                            name = "Interaction Name",
                            group = InteractionGroup.CARE,
                            isActive = true,
                            reminders = listOf(
                                Reminder(
                                    interactionId = "",
                                    dateTime = Clock.System.now().plus(1.hours).toLocalDateTime(TimeZone.currentSystemDefault())
                                )
                            )
                        )
                    )
                ),
                true,
                {}, {}, {}, { _, _, _ -> }
            )
        }
    }
}