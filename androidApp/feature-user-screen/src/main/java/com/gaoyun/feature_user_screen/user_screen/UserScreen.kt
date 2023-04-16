package com.gaoyun.feature_user_screen.user_screen

import android.app.Activity.RESULT_OK
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.gaoyun.common.R
import com.gaoyun.common.composables.*
import com.gaoyun.feature_user_screen.UserScreenContent
import com.gaoyun.roar.presentation.BackNavigationEffect
import com.gaoyun.roar.presentation.LAUNCH_LISTEN_FOR_EFFECTS
import com.gaoyun.roar.presentation.NavigationSideEffect
import com.gaoyun.roar.presentation.user_screen.UserScreenContract
import com.gaoyun.roar.presentation.user_screen.UserScreenViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserScreenDestination(
    onNavigationCall: (NavigationSideEffect) -> Unit,
) {
    val viewModel: UserScreenViewModel = getViewModel()
    val state = viewModel.viewState.collectAsState().value

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val exportBackupLauncher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        coroutineScope.launch {
            if (it.resultCode == RESULT_OK) {
                it.data?.data?.let { uri ->
                    context.contentResolver.openOutputStream(uri)?.use { stream ->
                        stream.write(viewModel.backupState.firstOrNull()?.toByteArray() ?: byteArrayOf())
                    }
                    delay(200)
                    snackbarHostState.showSnackbar(message = context.getString(R.string.backup_saved))
                }
            }
        }
    }

    LaunchedEffect(LAUNCH_LISTEN_FOR_EFFECTS) {
        viewModel.effect.onEach { effect ->
            when (effect) {
                is UserScreenContract.Effect.Navigation -> onNavigationCall(effect)
                is UserScreenContract.Effect.NavigateBack -> onNavigationCall(BackNavigationEffect)
                is UserScreenContract.Effect.BackupReady -> coroutineScope.launch {
                    val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
                        addCategory(Intent.CATEGORY_OPENABLE)
                        type = "application/json"
                        putExtra(Intent.EXTRA_TITLE, "Roar_Backup_${LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME)}.json")
                    }
                    exportBackupLauncher.launch(intent)
                }

                is UserScreenContract.Effect.BackupApplied -> snackbarHostState.showSnackbar(message = "Backup applied")
                is UserScreenContract.Effect.LoggedOut -> {
                    Firebase.auth.signOut()
                    viewModel.setEvent(UserScreenContract.Event.NavigateBack)
                }

                else -> {}
            }
        }.collect()
    }

    SurfaceScaffold(
        backHandler = { viewModel.setEvent(UserScreenContract.Event.NavigateBack) },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            RoarExtendedFAB(
                icon = Icons.Filled.Edit,
                contentDescription = stringResource(id = R.string.edit_profile),
                text = stringResource(id = R.string.edit),
                onClick = { viewModel.setEvent(UserScreenContract.Event.OnEditAccountClick) }
            )
        },
        floatingActionButtonPosition = FabPosition.End
    ) {
        UserScreenContent(
            state = state,
            onCreateBackupClick = viewModel::setEvent,
            onUseBackup = viewModel::setEvent,
            onNumberOfRemindersOnMainScreenChange = viewModel::setEvent,
            onDynamicColorsStateChange = viewModel::setEvent,
            onLogout = viewModel::setEvent,
        )
    }
}