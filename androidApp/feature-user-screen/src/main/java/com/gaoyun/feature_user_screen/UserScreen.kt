package com.gaoyun.feature_user_screen

import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavHostController
import com.gaoyun.common.OnLifecycleEvent
import com.gaoyun.common.ui.BoxWithLoader
import com.gaoyun.common.ui.RoarExtendedFloatingActionButton
import com.gaoyun.common.ui.Spacer
import com.gaoyun.common.ui.SurfaceScaffold
import com.gaoyun.roar.model.domain.User
import com.gaoyun.roar.presentation.LAUNCH_LISTEN_FOR_EFFECTS
import com.gaoyun.roar.presentation.user_screen.UserScreenContract
import com.gaoyun.roar.presentation.user_screen.UserScreenViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel
import java.io.FileInputStream
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun UserScreenDestination(
    navHostController: NavHostController,
) {
    val viewModel: UserScreenViewModel = getViewModel()
    val state = viewModel.viewState.collectAsState().value

    OnLifecycleEvent { _, event ->
        if (event == Lifecycle.Event.ON_CREATE) {
            viewModel.buildScreenState()
        }
    }

    UserScreen(
        state = state,
        effectFlow = viewModel.effect,
        onEventSent = { event -> viewModel.setEvent(event) },
        onNavigationRequested = { navigationEffect ->
            when (navigationEffect) {
                is UserScreenContract.Effect.Navigation.NavigateBack ->
                    navHostController.navigateUp()
            }
        },
        backupFlow = viewModel.backupState
    )

}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun UserScreen(
    state: UserScreenContract.State,
    effectFlow: Flow<UserScreenContract.Effect>,
    onEventSent: (event: UserScreenContract.Event) -> Unit,
    onNavigationRequested: (navigationEffect: UserScreenContract.Effect.Navigation) -> Unit,
    backupFlow: Flow<String>
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val importBackupLauncher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        println("selected file URI ${it.data?.data}")
        it.data?.data?.let { uri ->
            context.contentResolver.openFileDescriptor(uri, "r")?.use { descriptor ->
                if (descriptor.statSize <= Int.MAX_VALUE) {
                    val data = ByteArray(descriptor.statSize.toInt())
                    FileInputStream(descriptor.fileDescriptor).use { fileStream ->
                        fileStream.read(data)

                        AlertDialog.Builder(context)
                            .setTitle("Replace current data?")
                            .setMessage("We can leave current data without changes or replace it by backup completely")
                            .setPositiveButton("Leave") { dialog, _ ->
                                dialog.dismiss()
                                onEventSent(
                                    UserScreenContract.Event.OnUseBackup(
                                        backupString = String(data),
                                        removeOld = false
                                    )
                                )
                            }
                            .setNegativeButton("Replace") { dialog, _ ->
                                dialog.dismiss()
                                onEventSent(
                                    UserScreenContract.Event.OnUseBackup(
                                        backupString = String(data),
                                        removeOld = true
                                    )
                                )
                            }
                            .create()
                            .show()
                    }
                }
            }
        }
    }

    val exportBackupLauncher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        coroutineScope.launch {
            if (it.resultCode == RESULT_OK) {
                it.data?.data?.let { uri ->
                    context.contentResolver.openOutputStream(uri)?.use { stream ->
                        stream.write(backupFlow.firstOrNull()?.toByteArray() ?: byteArrayOf())
                    }
                    delay(200)
                    snackbarHostState.showSnackbar(message = "Backup saved")
                }
            }
        }
    }

    LaunchedEffect(LAUNCH_LISTEN_FOR_EFFECTS) {
        effectFlow.onEach { effect ->
            when (effect) {
                is UserScreenContract.Effect.Navigation -> onNavigationRequested(effect)
                is UserScreenContract.Effect.BackupReady -> coroutineScope.launch {
                    val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
                        addCategory(Intent.CATEGORY_OPENABLE)
                        type = "application/json"
                        putExtra(Intent.EXTRA_TITLE, "Roar_Backup_${LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME)}.json")
                    }
                    exportBackupLauncher.launch(intent)
                }
                is UserScreenContract.Effect.BackupApplied -> snackbarHostState.showSnackbar(message = "Backup applied")
                else -> {}
            }
        }.collect()
    }

    SurfaceScaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            RoarExtendedFloatingActionButton(
                icon = Icons.Filled.Edit,
                contentDescription = "Edit user",
                text = "Edit",
                onClick = { UserScreenContract.Event.OnEditAccountClick }
            )
        },
        floatingActionButtonPosition = FabPosition.End
    ) {
        BoxWithLoader(isLoading = state.user == null) {
            state.user?.let { user ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            start = 16.dp,
                            end = 16.dp,
                            top = WindowInsets.statusBars
                                .asPaddingValues()
                                .calculateTopPadding() + 32.dp
                        )
                ) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Hey, ${user.name}",
                            style = MaterialTheme.typography.displayMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Icon(
                            Icons.Default.Person, contentDescription = "", modifier = Modifier
                                .size(40.dp)
                                .padding(end = 8.dp, top = 8.dp)
                        )
                    }

                    Spacer(size = 8.dp)

                    Text(
                        text = "Thank you for being with us",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(size = 32.dp)

                    Text(
                        text = "Backup",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(size = 8.dp)

                    Row(modifier = Modifier.fillMaxWidth()) {
                        FilledTonalButton(
                            onClick = { onEventSent(UserScreenContract.Event.OnCreateBackupClick) },
                            modifier = Modifier
                                .weight(1f)
                                .padding(end = 4.dp)
                        ) {
                            Icon(Icons.Filled.Save, contentDescription = "")
                            Spacer(size = 6.dp)
                            Text("Export", style = MaterialTheme.typography.titleMedium)
                        }

                        FilledTonalButton(
                            onClick = {
                                val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                                    addCategory(Intent.CATEGORY_OPENABLE)
                                    type = "application/json"
                                }
                                importBackupLauncher.launch(intent)
                            },
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 4.dp)
                        ) {
                            Icon(Icons.Filled.Download, contentDescription = "")
                            Spacer(size = 6.dp)
                            Text("Import", style = MaterialTheme.typography.titleMedium)
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun UserScreenPreview() {
    UserScreen(
        state = UserScreenContract.State(false, User("id", "Tester")),
        effectFlow = emptyFlow(),
        onEventSent = {},
        onNavigationRequested = {},
        backupFlow = flowOf()
    )
}