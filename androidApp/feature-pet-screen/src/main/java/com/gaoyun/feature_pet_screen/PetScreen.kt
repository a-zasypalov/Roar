package com.gaoyun.feature_pet_screen

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.gaoyun.roar.model.domain.Gender
import com.gaoyun.roar.model.domain.Pet
import com.gaoyun.roar.model.domain.PetType
import com.gaoyun.roar.model.domain.Reminder
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
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.koin.androidx.compose.getViewModel
import kotlin.time.Duration.Companion.hours

@Composable
fun PetScreenDestination(
    navHostController: NavHostController,
    petId: String
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
                is PetScreenContract.Effect.Navigation.ToInteractionDetails -> navHostController.navigate(
                    "${NavigationKeys.Route.INTERACTION_DETAIL}/${navigationEffect.interactionId}"
                )
                is PetScreenContract.Effect.Navigation.ToInteractionTemplates -> navigationEffect.petId?.let { petId ->
                    navHostController.navigate("${NavigationKeys.Route.ADD_REMINDER}/$petId")
                }
                is PetScreenContract.Effect.Navigation.NavigateBack -> navHostController.navigateUp()
            }
        },
    )

}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun PetScreen(
    state: PetScreenContract.State,
    effectFlow: Flow<PetScreenContract.Effect>,
    onEventSent: (event: PetScreenContract.Event) -> Unit,
    onNavigationRequested: (navigationEffect: PetScreenContract.Effect.Navigation) -> Unit,
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
                onClick = { onEventSent(PetScreenContract.Event.AddReminderButtonClicked(state.pet?.id)) })
        },
        floatingActionButtonPosition = FabPosition.End
    ) {
        Box {
            state.pet?.let { pet ->
                PetContainer(
                    pet = pet,
                    interactions = state.interactions,
                    onInteractionClick = { onEventSent(PetScreenContract.Event.InteractionClicked(it)) }
                )
            }

            Loader(isLoading = state.isLoading)
        }
    }
}

@Composable
private fun PetContainer(
    pet: Pet,
    interactions: List<InteractionWithReminders>,
    onInteractionClick: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .padding(start = 8.dp, end = 8.dp)
            .fillMaxWidth()
    ) {
        item {
            PetHeader(
                pet = pet, modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 32.dp, bottom = 8.dp)
            )
        }
        item {
            Text(
                text = "Reminders",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(vertical = 16.dp, horizontal = 8.dp)
            )
        }

        interactions.groupBy { it.group }.toSortedMap().map {
            item {
                Text(
                    text = it.key.toString(),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(vertical = 8.dp, horizontal = 8.dp)
                )
            }

            items(it.value) { interaction ->
                InteractionCard(interaction, onInteractionClick)
            }
        }

        item { Spacer(size = 120.dp) }
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
                ),
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
            ) {}
        }
    }
}