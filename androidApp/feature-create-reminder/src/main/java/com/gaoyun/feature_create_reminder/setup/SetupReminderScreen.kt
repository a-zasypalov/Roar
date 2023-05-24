package com.gaoyun.feature_create_reminder.setup

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import com.gaoyun.common.OnLifecycleEvent
import com.gaoyun.common.composables.*
import com.gaoyun.common.ext.getDrawableByName
import com.gaoyun.roar.model.domain.interactions.*
import com.gaoyun.roar.presentation.BackNavigationEffect
import com.gaoyun.roar.presentation.LAUNCH_LISTEN_FOR_EFFECTS
import com.gaoyun.roar.presentation.NavigationSideEffect
import com.gaoyun.roar.presentation.add_reminder.setup_reminder.SetupReminderScreenContract
import com.gaoyun.roar.presentation.add_reminder.setup_reminder.SetupReminderScreenViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.datetime.*
import org.koin.androidx.compose.getViewModel
import com.gaoyun.common.R as CommonR

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetupReminderDestination(
    onNavigationCall: (NavigationSideEffect) -> Unit,
    petId: String,
    templateId: String,
    interactionId: String? = null
) {
    val viewModel: SetupReminderScreenViewModel = getViewModel()
    val state = viewModel.viewState.collectAsState().value
    val snackbarHostState = remember { SnackbarHostState() }

    OnLifecycleEvent { _, event ->
        if (event == Lifecycle.Event.ON_CREATE) {
            viewModel.buildScreenState(petId = petId, templateId = templateId, interactionId = interactionId)
        }
    }

    val avatar = remember { mutableStateOf("ic_cat") }

    LaunchedEffect(LAUNCH_LISTEN_FOR_EFFECTS) {
        viewModel.effect.onEach { effect ->
            when (effect) {
                is SetupReminderScreenContract.Effect.Navigation -> onNavigationCall(effect)
                is SetupReminderScreenContract.Effect.ReminderSaved -> onNavigationCall(BackNavigationEffect)
            }
        }.collect()
    }

    SurfaceScaffold(
        backHandler = { onNavigationCall(BackNavigationEffect) },
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) {
        BoxWithLoader(isLoading = state.isLoading) {
            state.pet?.let { pet ->
                avatar.value = pet.avatar

                Column(
                    verticalArrangement = Arrangement.Bottom,// Alignment.BottomCenter,
                    modifier = Modifier.fillMaxSize()
                ) {
                    ReminderSetupHeader(
                        petAvatar = pet.avatar,
                        petName = pet.name,
                    )

                    SurfaceCard(
                        shape = surfaceCardFormShape,
                        elevation = surfaceCardFormElevation(),
                        modifier = Modifier.padding(horizontal = 6.dp).padding(top = 8.dp),
                    ) {
                        ReminderSetupForm(
                            interactionToEdit = state.interactionToEdit,
                            template = state.template,
                            repeatConfig = state.repeatConfig,
                            remindConfig = state.remindConfig,
                            snackbarHost = snackbarHostState,
                            onRepeatConfigSave = { config ->
                                viewModel.setEvent(SetupReminderScreenContract.Event.RepeatConfigChanged(config))
                            },
                            onRemindConfigSave = { config ->
                                viewModel.setEvent(SetupReminderScreenContract.Event.RemindConfigChanged(config))
                            },
                            onSaveButtonClick = { name, type, group, repeatIsEnabled, repeatConfig, notes, date, timeHours, timeMinutes, remindConfig ->
                                viewModel.setEvent(
                                    SetupReminderScreenContract.Event.OnSaveButtonClick(
                                        name = name,
                                        type = type,
                                        group = group,
                                        repeatIsEnabled = repeatIsEnabled,
                                        repeatConfig = repeatConfig,
                                        remindConfig = remindConfig,
                                        notes = notes,
                                        petId = pet.id,
                                        templateId = state.template?.id,
                                        date = date,
                                        timeHours = timeHours,
                                        timeMinutes = timeMinutes
                                    )
                                )
                            },
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ReminderSetupHeader(
    petAvatar: String,
    petName: String,
) {
//    Box(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .padding(top = 8.dp, start = 16.dp, end = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = LocalContext.current.getDrawableByName(petAvatar)),
                contentDescription = petName,
                modifier = Modifier.size(48.dp)
            )

            Spacer(size = 10.dp)

            Text(
                text = stringResource(id = CommonR.string.reminder),
                style = MaterialTheme.typography.displayMedium,
            )
        }
//    }
}