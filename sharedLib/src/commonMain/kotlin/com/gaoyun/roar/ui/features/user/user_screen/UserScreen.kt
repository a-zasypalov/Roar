package com.gaoyun.roar.ui.features.user.user_screen

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.FabPosition
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import com.gaoyun.roar.presentation.BackNavigationEffect
import com.gaoyun.roar.presentation.LAUNCH_LISTEN_FOR_EFFECTS
import com.gaoyun.roar.presentation.NavigationSideEffect
import com.gaoyun.roar.presentation.user_screen.UserScreenContract
import com.gaoyun.roar.presentation.user_screen.UserScreenViewModel
import com.gaoyun.roar.ui.common.composables.RoarExtendedFAB
import com.gaoyun.roar.ui.common.composables.SurfaceScaffold
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import moe.tlaster.precompose.koin.koinViewModel
import org.jetbrains.compose.resources.stringResource
import roar.sharedlib.generated.resources.Res
import roar.sharedlib.generated.resources.backup_applied
import roar.sharedlib.generated.resources.backup_saved
import roar.sharedlib.generated.resources.edit
import roar.sharedlib.generated.resources.edit_profile

@Composable
fun UserScreenDestination(
    onNavigationCall: (NavigationSideEffect) -> Unit,
) {
    val viewModel = koinViewModel(vmClass = UserScreenViewModel::class)
    val state = viewModel.viewState.collectAsState().value

    val snackbarHostState = remember { SnackbarHostState() }

    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
        viewModel.buildScreenState()
    }

    val backupSavedMessage = stringResource(Res.string.backup_saved)
    val backupAppliedMessage = stringResource(Res.string.backup_applied)

    LaunchedEffect(LAUNCH_LISTEN_FOR_EFFECTS) {
        viewModel.effect.onEach { effect ->
            when (effect) {
                is UserScreenContract.Effect.NavigateBack -> onNavigationCall(BackNavigationEffect)
                is UserScreenContract.Effect.Navigation -> onNavigationCall(effect)
                is UserScreenContract.Effect.BackupCreated -> snackbarHostState.showSnackbar(message = backupSavedMessage)
                is UserScreenContract.Effect.BackupApplied -> snackbarHostState.showSnackbar(message = backupAppliedMessage)
                is UserScreenContract.Effect.LoggedOut -> viewModel.setEvent(UserScreenContract.Event.NavigateBack)
            }
        }.collect()
    }

    SurfaceScaffold(
        backHandler = { viewModel.setEvent(UserScreenContract.Event.NavigateBack) },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            RoarExtendedFAB(
                icon = Icons.Filled.Edit,
                contentDescription = stringResource(resource = Res.string.edit_profile),
                text = stringResource(resource = Res.string.edit),
                onClick = { viewModel.setEvent(UserScreenContract.Event.OnEditAccountClick) }
            )
        },
        floatingActionButtonPosition = FabPosition.End
    ) {
        UserScreenContent(
            state = state,
            onCreateBackupClick = viewModel::setEvent,
            onNumberOfRemindersOnMainScreenChange = viewModel::setEvent,
            onDynamicColorsStateChange = viewModel::setEvent,
            onStaticColorThemePick = viewModel::setEvent,
            onLogout = viewModel::setEvent,
            onAboutScreenButtonClick = viewModel::setEvent,
            onHomeScreenModeChange = viewModel::setEvent,
            onIconChange = viewModel::setEvent,
            onAccountDeleteConfirmed = viewModel::setEvent
        )
    }
}