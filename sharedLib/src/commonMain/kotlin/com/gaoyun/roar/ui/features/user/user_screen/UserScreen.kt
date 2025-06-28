package com.gaoyun.roar.ui.features.user.user_screen

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.FabPosition
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.gaoyun.roar.presentation.user_screen.UserScreenContract
import com.gaoyun.roar.presentation.user_screen.UserScreenViewModel
import com.gaoyun.roar.ui.common.composables.RoarExtendedFAB
import com.gaoyun.roar.ui.common.composables.SurfaceScaffold
import com.gaoyun.roar.ui.navigation.BackNavigationEffect
import com.gaoyun.roar.ui.navigation.NavigationSideEffect
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import roar.sharedlib.generated.resources.Res
import roar.sharedlib.generated.resources.backup_applied
import roar.sharedlib.generated.resources.backup_saved
import roar.sharedlib.generated.resources.edit
import roar.sharedlib.generated.resources.edit_profile

@Composable
fun UserScreenDestination(
    navigate: (NavigationSideEffect) -> Unit,
) {
    val viewModel = koinViewModel<UserScreenViewModel>()
    val state by viewModel.viewState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewModel.loadScreen()
    }

    LaunchedEffect(Unit) {
        viewModel.messages.collect { msg ->
            snackbarHostState.showSnackbar(msg)
        }
    }

    val backupSavedMessage = stringResource(Res.string.backup_saved)
    val backupAppliedMessage = stringResource(Res.string.backup_applied)

    SurfaceScaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        backHandler = { navigate(BackNavigationEffect) },
        floatingActionButton = {
            RoarExtendedFAB(
                icon = Icons.Filled.Edit,
                contentDescription = stringResource(resource = Res.string.edit_profile),
                text = stringResource(resource = Res.string.edit),
                onClick = { navigate(UserScreenContract.Effect.Navigation.ToUserEdit) }
            )
        },
        floatingActionButtonPosition = FabPosition.End
    ) {
        UserScreenContent(
            state = state,
            onCreateBackupClick = {
                when (it) {
                    is UserScreenContract.Event.OnCreateBackupClick -> viewModel.createBackup {
                        scope.launch { snackbarHostState.showSnackbar(backupSavedMessage) }
                    }

                    is UserScreenContract.Event.OnUseBackupClick -> viewModel.triggerBackupImport {
                        scope.launch { snackbarHostState.showSnackbar(backupAppliedMessage) }
                    }

                    else -> {}
                }
            },
            onNumberOfRemindersOnMainScreenChange = {
                viewModel.setNumberOfRemindersOnMainScreen(it.newNumber)
            },
            onDynamicColorsStateChange = {
                viewModel.toggleDynamicColor(it.active)
            },
            onStaticColorThemePick = {
                viewModel.changeStaticTheme(it.theme)
            },
            onLogout = {
                viewModel.logout {
                    navigate(BackNavigationEffect)
                }
            },
            onAboutScreenButtonClick = {
                navigate(UserScreenContract.Effect.Navigation.ToAboutScreen)
            },
            onHomeScreenModeChange = {
                viewModel.switchHomeScreenMode()
            },
            onIconChange = {
                viewModel.changeAppIcon(it.icon)
            },
            onAccountDeleteConfirmed = {
                viewModel.deleteAccount {
                    navigate(BackNavigationEffect)
                }
            }
        )
    }
}