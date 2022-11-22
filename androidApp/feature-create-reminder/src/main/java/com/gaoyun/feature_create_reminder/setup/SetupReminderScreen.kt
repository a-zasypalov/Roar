package com.gaoyun.feature_create_reminder.setup

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavHostController
import com.gaoyun.common.NavigationKeys.RouteGlobal.ADD_REMINDER
import com.gaoyun.common.OnLifecycleEvent
import com.gaoyun.common.ui.*
import com.gaoyun.feature_create_reminder.notification.ReminderBroadcastReceiver
import com.gaoyun.feature_create_reminder.notification.putReminderBroadcastReceiverArguments
import com.gaoyun.roar.model.domain.Reminder
import com.gaoyun.roar.model.domain.interactions.*
import com.gaoyun.roar.presentation.LAUNCH_LISTEN_FOR_EFFECTS
import com.gaoyun.roar.presentation.add_reminder.setup_reminder.SetupReminderScreenContract
import com.gaoyun.roar.presentation.add_reminder.setup_reminder.SetupReminderScreenViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.datetime.*
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
                is SetupReminderScreenContract.Effect.Navigation.ToComplete -> navHostController.navigate(
                    "$ADD_REMINDER/$petId/$templateId/${navigationEffect.petAvatar}"
                )
                is SetupReminderScreenContract.Effect.Navigation.NavigateBack -> navHostController.navigateUp()
            }
        }
    )
}

@SuppressLint("MissingPermission")
fun scheduleNotification(context: Context, reminder: Reminder) {
    val intent = Intent(context, ReminderBroadcastReceiver::class.java)
    intent.putReminderBroadcastReceiverArguments(
        reminderId = reminder.id
    )

    val pendingIntent = PendingIntent.getBroadcast(context, 101, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
    val alarmManager = getSystemService(context, AlarmManager::class.java)
    alarmManager?.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, reminder.dateTime.toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds(), pendingIntent)
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun SetupReminderScreen(
    state: SetupReminderScreenContract.State,
    effectFlow: Flow<SetupReminderScreenContract.Effect>,
    onEventSent: (event: SetupReminderScreenContract.Event) -> Unit,
    onNavigationRequested: (navigationEffect: SetupReminderScreenContract.Effect.Navigation) -> Unit,
) {
    val context = LocalContext.current
    val avatar = remember { mutableStateOf("ic_cat") }

    LaunchedEffect(LAUNCH_LISTEN_FOR_EFFECTS) {
        effectFlow.onEach { effect ->
            when (effect) {
                is SetupReminderScreenContract.Effect.ReminderCreated -> {
                    scheduleNotification(context, effect.reminder)
                    onNavigationRequested(SetupReminderScreenContract.Effect.Navigation.ToComplete(avatar.value))
                }
                else -> {}
            }
        }.collect()
    }

    SurfaceScaffold {
        Box(contentAlignment = Alignment.BottomCenter) {
            state.pet?.let { pet ->
                avatar.value = pet.avatar

                Box(modifier = Modifier.fillMaxSize()) {
                    ReminderSetupHeader(
                        petAvatar = pet.avatar,
                        petName = pet.name,
                    )
                }

                Column {
                    Spacer(size = 110.dp)
                    SurfaceCard(
                        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp, bottomStart = 0.dp, bottomEnd = 0.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                        modifier = Modifier.padding(horizontal = 12.dp)
                    ) {
                        Box(modifier = Modifier.padding(top = 32.dp)) {
                            ReminderSetupForm(
                                template = state.template,
                                repeatConfig = state.repeatConfig,
                                onConfigSave = { config ->
                                    onEventSent(SetupReminderScreenContract.Event.RepeatConfigChanged(config))
                                },
                                onSaveButtonClick = { name, type, group, repeatIsEnabled, repeatConfig, notes, date, timeHours, timeMinutes ->
                                    onEventSent(
                                        SetupReminderScreenContract.Event.OnSaveButtonClick(
                                            name = name,
                                            type = type,
                                            group = group,
                                            repeatIsEnabled = repeatIsEnabled,
                                            repeatConfig = repeatConfig,
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

            Loader(isLoading = state.isLoading)
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
            .padding(top = 32.dp, start = 16.dp, end = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = LocalContext.current.getDrawableByName(petAvatar)),
            contentDescription = petName,
            modifier = Modifier.size(48.dp)
        )

        Spacer(size = 10.dp)

        Text(
            text = "Reminder",
            style = MaterialTheme.typography.displayMedium,
        )
    }
}