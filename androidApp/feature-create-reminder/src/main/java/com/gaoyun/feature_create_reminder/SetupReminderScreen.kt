package com.gaoyun.feature_create_reminder

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavHostController
import com.gaoyun.common.OnLifecycleEvent
import com.gaoyun.common.ui.*
import com.gaoyun.roar.presentation.LAUNCH_LISTEN_FOR_EFFECTS
import com.gaoyun.roar.presentation.add_reminder.setup_reminder.SetupReminderScreenContract
import com.gaoyun.roar.presentation.add_reminder.setup_reminder.SetupReminderScreenViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.compose.getViewModel

@Composable
fun SetupReminderDestination(
    navHostController: NavHostController,
    petId: String,
    templateId: String
) {
    val viewModel: SetupReminderScreenViewModel = getViewModel()
    val state = viewModel.viewState.collectAsState().value

    OnLifecycleEvent { _, event ->
        if (event == Lifecycle.Event.ON_CREATE) {
            viewModel.buildScreenState(petId = petId, templateId = templateId)
        }
    }

    SetupReminderScreen(
        state = state,
        effectFlow = viewModel.effect,
        onEventSent = { event -> viewModel.setEvent(event) },
        onNavigationRequested = { navigationEffect ->
            when (navigationEffect) {
                is SetupReminderScreenContract.Effect.Navigation.NavigateBack -> navHostController.navigateUp()
            }
        },
    )

}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun SetupReminderScreen(
    state: SetupReminderScreenContract.State,
    effectFlow: Flow<SetupReminderScreenContract.Effect>,
    onEventSent: (event: SetupReminderScreenContract.Event) -> Unit,
    onNavigationRequested: (navigationEffect: SetupReminderScreenContract.Effect.Navigation) -> Unit,
) {

    LaunchedEffect(LAUNCH_LISTEN_FOR_EFFECTS) {
        effectFlow.onEach { effect ->
            when (effect) {
//                is SetupReminderScreenContract.Effect.TemplateChosen -> {
//                    onNavigationRequested(AddReminderScreenContract.Effect.Navigation.ToReminderSetup(effect.templateId))
//                }
                else -> {}
            }
        }.collect()
    }

    SurfaceScaffold {
        Box {
            LazyColumn {
                state.pet?.let { pet ->
                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 16.dp, top = 32.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                painter = painterResource(id = LocalContext.current.getDrawableByName(pet.avatar)),
                                contentDescription = pet.name,
                                modifier = Modifier.size(48.dp)
                            )

                            Spacer(size = 10.dp)

                            Text(
                                text = "Reminder",
                                style = MaterialTheme.typography.displayMedium,
                            )
                        }
                    }
                }
            }
            Loader(isLoading = state.isLoading)
        }
    }
}