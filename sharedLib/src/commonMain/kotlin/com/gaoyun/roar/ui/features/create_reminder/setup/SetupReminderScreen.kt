package com.gaoyun.roar.ui.features.create_reminder.setup

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import com.gaoyun.roar.presentation.BackNavigationEffect
import com.gaoyun.roar.presentation.LAUNCH_LISTEN_FOR_EFFECTS
import com.gaoyun.roar.presentation.NavigationSideEffect
import com.gaoyun.roar.presentation.add_reminder.setup_reminder.SetupReminderScreenContract
import com.gaoyun.roar.presentation.add_reminder.setup_reminder.SetupReminderScreenViewModel
import com.gaoyun.roar.ui.common.composables.AutoResizeText
import com.gaoyun.roar.ui.common.composables.BoxWithLoader
import com.gaoyun.roar.ui.common.composables.FontSizeRange
import com.gaoyun.roar.ui.common.composables.Spacer
import com.gaoyun.roar.ui.common.composables.SurfaceCard
import com.gaoyun.roar.ui.common.composables.SurfaceScaffold
import com.gaoyun.roar.ui.common.composables.surfaceCardFormElevation
import com.gaoyun.roar.ui.common.composables.surfaceCardFormShape
import com.gaoyun.roar.ui.common.ext.getDrawableByName
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import moe.tlaster.precompose.koin.koinViewModel
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import roar.sharedlib.generated.resources.Res
import roar.sharedlib.generated.resources.reminder

@Composable
fun SetupReminderDestination(
    onNavigationCall: (NavigationSideEffect) -> Unit,
    petId: String,
    templateId: String,
    interactionId: String? = null
) {
    val viewModel = koinViewModel(vmClass = SetupReminderScreenViewModel::class)
    val state = viewModel.viewState.collectAsState().value
    val snackbarHostState = remember { SnackbarHostState() }

    LifecycleEventEffect(Lifecycle.Event.ON_CREATE) {
        viewModel.buildScreenState(petId = petId, templateId = templateId, interactionId = interactionId)
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
                    verticalArrangement = Arrangement.Bottom,
                    modifier = Modifier.fillMaxSize()
                ) {
                    ReminderSetupHeader(
                        petAvatar = pet.avatar,
                        petName = pet.name,
                    )

                    SurfaceCard(
                        shape = surfaceCardFormShape,
                        elevation = surfaceCardFormElevation(),
                        modifier = Modifier
                            .padding(horizontal = 6.dp)
                            .padding(top = 8.dp),
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
    Row(
        modifier = Modifier
            .padding(top = 8.dp, start = 16.dp, end = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(getDrawableByName(petAvatar)),
            contentDescription = petName,
            modifier = Modifier.size(48.dp)
        )

        Spacer(size = 10.dp)

        AutoResizeText(
            text = stringResource(resource = Res.string.reminder),
            fontSizeRange = FontSizeRange(
                min = 20.sp,
                max = MaterialTheme.typography.displayMedium.fontSize,
            ),
            style = MaterialTheme.typography.displayMedium,
        )
    }
}