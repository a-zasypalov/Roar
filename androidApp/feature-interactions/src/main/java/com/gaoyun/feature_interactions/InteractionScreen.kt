package com.gaoyun.feature_interactions

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavHostController
import com.gaoyun.common.OnLifecycleEvent
import com.gaoyun.common.ui.*
import com.gaoyun.roar.presentation.LAUNCH_LISTEN_FOR_EFFECTS
import com.gaoyun.roar.presentation.interactions.InteractionScreenContract
import com.gaoyun.roar.presentation.interactions.InteractionScreenViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.compose.getViewModel

@Composable
fun InteractionScreenDestination(
    navHostController: NavHostController,
    interactionId: String
) {
    val viewModel: InteractionScreenViewModel = getViewModel()
    val state = viewModel.viewState.collectAsState().value
    val notesState = rememberSaveable { mutableStateOf(state.interaction?.notes) }

    if (notesState.value == null && !state.interaction?.notes.isNullOrEmpty()) {
        notesState.value = state.interaction?.notes.orEmpty()
    }

    OnLifecycleEvent { _, event ->
        if (event == Lifecycle.Event.ON_CREATE) {
            viewModel.buildScreenState(interactionId)
        } else if (event == Lifecycle.Event.ON_PAUSE) {
            viewModel.setEvent(InteractionScreenContract.Event.OnSaveNotes(notesState.value ?: ""))
        }
    }

    InteractionScreen(
        state = state,
        effectFlow = viewModel.effect,
        notesState = notesState,
        onEventSent = { event -> viewModel.setEvent(event) },
        onNavigationRequested = { navigationEffect ->
            when (navigationEffect) {
                is InteractionScreenContract.Effect.Navigation.NavigateBack -> navHostController.navigateUp()
            }
        },
    )

}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun InteractionScreen(
    state: InteractionScreenContract.State,
    effectFlow: Flow<InteractionScreenContract.Effect>,
    onEventSent: (event: InteractionScreenContract.Event) -> Unit,
    onNavigationRequested: (navigationEffect: InteractionScreenContract.Effect.Navigation) -> Unit,
    notesState: MutableState<String?>
) {
    LaunchedEffect(LAUNCH_LISTEN_FOR_EFFECTS) {
        effectFlow.onEach { effect ->
            when (effect) {
                is InteractionScreenContract.Effect.Navigation -> onNavigationRequested(effect)
                else -> {}
            }
        }.collect()
    }

    SurfaceScaffold(
        floatingActionButton = {
            RoarExtendedFloatingActionButton(
                icon = Icons.Filled.Edit,
                contentDescription = "Edit",
                text = "Edit",
                onClick = { onEventSent(InteractionScreenContract.Event.OnEditButtonClick(state.interaction?.id)) })
        },
        floatingActionButtonPosition = FabPosition.End
    ) {
        Box {
            state.interaction?.let { interaction ->
                state.pet?.let { pet ->
                    val nextReminders = interaction.reminders.filter { !it.isCompleted }
                    val completeReminders = interaction.reminders.filter { !it.isCompleted }

                    LazyColumn(modifier = Modifier.padding(horizontal = 8.dp)) {
                        item {
                            InteractionHeader(
                                pet = pet,
                                interaction = interaction,
                                notesState = notesState,
                                modifier = Modifier.padding(top = 32.dp)
                            )
                        }

                        if (nextReminders.isNotEmpty()) {
                            item {
                                Text(
                                    text = "Next",
                                    style = MaterialTheme.typography.headlineMedium,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier.padding(vertical = 16.dp, horizontal = 8.dp)
                                )
                            }

                            item {
                                Surface(
                                    tonalElevation = 4.dp,
                                    shape = MaterialTheme.shapes.large,
                                    modifier = Modifier
                                        .padding(horizontal = 8.dp, vertical = 8.dp)
                                        .fillMaxWidth(),
                                ) {
                                    Column {
                                        nextReminders.map { reminder ->
                                            LabelledCheckBox(
                                                checked = reminder.isCompleted,
                                                label = "${reminder.dateTime.date} at 09:00",
                                                modifier = Modifier.fillMaxWidth(),
                                                verticalPadding = 16.dp,
                                                horizontalPadding = 20.dp,
                                                spacerSize = 14.dp,
                                                onCheckedChange = {}
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        if (completeReminders.isNotEmpty()) {
                            item {
                                Text(
                                    text = "History",
                                    style = MaterialTheme.typography.headlineMedium,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier.padding(vertical = 16.dp, horizontal = 8.dp)
                                )
                            }

                            item {
                                Surface(
                                    tonalElevation = 4.dp,
                                    shape = MaterialTheme.shapes.large,
                                    modifier = Modifier
                                        .padding(horizontal = 8.dp, vertical = 8.dp)
                                        .fillMaxWidth(),
                                ) {
                                    Column {
                                        completeReminders.map { reminder ->
                                            LabelledCheckBox(
                                                checked = !reminder.isCompleted,
                                                label = "${reminder.dateTime.date} at 09:00",
                                                modifier = Modifier.fillMaxWidth(),
                                                verticalPadding = 16.dp,
                                                horizontalPadding = 20.dp,
                                                spacerSize = 14.dp,
                                                onCheckedChange = {}
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        item { Spacer(size = 120.dp) }
                    }
                }
            }
        }

        Loader(isLoading = state.isLoading)
    }
}

@Preview
@Composable
fun InteractionScreenPreview() {

}