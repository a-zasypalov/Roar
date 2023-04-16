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
import androidx.navigation.NavHostController
import com.gaoyun.common.NavigationKeys.Route.ADD_REMINDER
import com.gaoyun.common.OnLifecycleEvent
import com.gaoyun.common.composables.*
import com.gaoyun.common.ext.getDrawableByName
import com.gaoyun.roar.model.domain.interactions.*
import com.gaoyun.roar.presentation.LAUNCH_LISTEN_FOR_EFFECTS
import com.gaoyun.roar.presentation.add_reminder.setup_reminder.SetupReminderScreenContract
import com.gaoyun.roar.presentation.add_reminder.setup_reminder.SetupReminderScreenViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.datetime.*
import org.koin.androidx.compose.getViewModel
import com.gaoyun.common.R as CommonR

@Composable
fun SetupReminderDestination(
    navHostController: NavHostController,
    petId: String,
    templateId: String,
    interactionId: String? = null
) {
    val viewModel: SetupReminderScreenViewModel = getViewModel()
    val state = viewModel.viewState.collectAsState().value

    OnLifecycleEvent { _, event ->
        if (event == Lifecycle.Event.ON_CREATE) {
            viewModel.buildScreenState(petId = petId, templateId = templateId, interactionId = interactionId)
        }
    }

    SetupReminderScreen(
        state = state,
        effectFlow = viewModel.effect,
        onEventSent = { event -> viewModel.setEvent(event) },
        onNavigationRequested = { navigationEffect ->
            when (navigationEffect) {
                is SetupReminderScreenContract.Effect.Navigation.ToComplete -> navHostController.navigate(
                    "$ADD_REMINDER/$petId/$templateId/${navigationEffect.petAvatar}"
                )

                is SetupReminderScreenContract.Effect.Navigation.NavigateBack -> navHostController.navigateUp()
            }
        }
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
    val avatar = remember { mutableStateOf("ic_cat") }

    LaunchedEffect(LAUNCH_LISTEN_FOR_EFFECTS) {
        effectFlow.onEach { effect ->
            when (effect) {
                is SetupReminderScreenContract.Effect.ReminderCreated -> onNavigationRequested(SetupReminderScreenContract.Effect.Navigation.ToComplete(avatar.value))
                is SetupReminderScreenContract.Effect.ReminderSaved -> onNavigationRequested(SetupReminderScreenContract.Effect.Navigation.NavigateBack)
                else -> {}
            }
        }.collect()
    }

    SurfaceScaffold(
        backHandler = { onNavigationRequested(SetupReminderScreenContract.Effect.Navigation.NavigateBack) },
    ) {
        BoxWithLoader(isLoading = state.isLoading) {
            state.pet?.let { pet ->
                avatar.value = pet.avatar

                Box(
                    contentAlignment = Alignment.BottomCenter,
                    modifier = Modifier.fillMaxSize()
                ) {
                    ReminderSetupHeader(
                        petAvatar = pet.avatar,
                        petName = pet.name,
                    )

                    SurfaceCard(
                        shape = surfaceCardFormShape,
                        elevation = surfaceCardFormElevation(),
                        modifier = Modifier.padding(horizontal = 6.dp)
                    ) {
                        ReminderSetupForm(
                            interactionToEdit = state.interactionToEdit,
                            template = state.template,
                            repeatConfig = state.repeatConfig,
                            remindConfig = state.remindConfig,
                            onRepeatConfigSave = { config ->
                                onEventSent(SetupReminderScreenContract.Event.RepeatConfigChanged(config))
                            },
                            onRemindConfigSave = { config ->
                                onEventSent(SetupReminderScreenContract.Event.RemindConfigChanged(config))
                            },
                            onSaveButtonClick = { name, type, group, repeatIsEnabled, repeatConfig, notes, date, timeHours, timeMinutes, remindConfig ->
                                onEventSent(
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
    Box(modifier = Modifier.fillMaxSize()) {
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
    }
}