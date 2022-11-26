package com.gaoyun.feature_interactions

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavHostController
import com.gaoyun.common.OnLifecycleEvent
import com.gaoyun.common.ui.LabelledCheckBox
import com.gaoyun.common.ui.Loader
import com.gaoyun.common.ui.RoarExtendedFloatingActionButton
import com.gaoyun.common.ui.SurfaceScaffold
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

    OnLifecycleEvent { _, event ->
        if (event == Lifecycle.Event.ON_CREATE) {
            viewModel.buildScreenState(interactionId)
        }
    }

    InteractionScreen(
        state = state,
        effectFlow = viewModel.effect,
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
                    LazyColumn(modifier = Modifier.padding(horizontal = 8.dp)) {
                        item {
                            InteractionHeader(
                                pet = pet,
                                interaction = interaction,
                                modifier = Modifier.padding(top = 32.dp)
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

                        item {
                            Surface(
                                tonalElevation = 4.dp,
                                shape = MaterialTheme.shapes.large,
                                modifier = Modifier
                                    .padding(horizontal = 8.dp, vertical = 8.dp)
                                    .fillMaxWidth(),
                            ) {
                                Column {
                                    interaction.reminders.map { reminder ->
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
                                    interaction.reminders.map { reminder ->
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
                                    interaction.reminders.map { reminder ->
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