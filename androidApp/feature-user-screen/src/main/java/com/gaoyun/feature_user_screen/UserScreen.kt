package com.gaoyun.feature_user_screen

import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavHostController
import com.gaoyun.common.NavigationKeys
import com.gaoyun.common.OnLifecycleEvent
import com.gaoyun.common.R
import com.gaoyun.common.ui.*
import com.gaoyun.roar.model.domain.User
import com.gaoyun.roar.presentation.LAUNCH_LISTEN_FOR_EFFECTS
import com.gaoyun.roar.presentation.user_screen.UserScreenContract
import com.gaoyun.roar.presentation.user_screen.UserScreenViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
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
                is UserScreenContract.Effect.Navigation.NavigateBack -> navHostController.navigateUp()
                is UserScreenContract.Effect.Navigation.ToUserEdit -> navHostController.navigate(NavigationKeys.Route.USER_EDIT_ROUTE)
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
    val activity = LocalContext.current as AppCompatActivity
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val numberOfRemindersOnMainScreenState = remember { mutableStateOf(state.numberOfRemindersOnMainScreenState) }
    numberOfRemindersOnMainScreenState.value = state.numberOfRemindersOnMainScreenState

    val importBackupLauncher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        it.data?.data?.let { uri ->
            context.contentResolver.openFileDescriptor(uri, "r")?.use { descriptor ->
                if (descriptor.statSize <= Int.MAX_VALUE) {
                    val data = ByteArray(descriptor.statSize.toInt())
                    FileInputStream(descriptor.fileDescriptor).use { fileStream ->
                        fileStream.read(data)

                        AlertDialog.Builder(context)
                            .setTitle(context.getString(R.string.replace_current_data_title))
                            .setMessage(context.getString(R.string.replace_current_data_description))
                            .setPositiveButton(context.getString(R.string.leave)) { dialog, _ ->
                                dialog.dismiss()
                                onEventSent(
                                    UserScreenContract.Event.OnUseBackup(
                                        backup = data,
                                        removeOld = false
                                    )
                                )
                            }
                            .setNegativeButton(context.getString(R.string.replace)) { dialog, _ ->
                                dialog.dismiss()
                                onEventSent(
                                    UserScreenContract.Event.OnUseBackup(
                                        backup = data,
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
                    snackbarHostState.showSnackbar(message = context.getString(R.string.backup_saved))
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
                is UserScreenContract.Effect.LoggedOut -> {
                    Firebase.auth.signOut()
                    onNavigationRequested(UserScreenContract.Effect.Navigation.NavigateBack)
                }
                else -> {}
            }
        }.collect()
    }

    SurfaceScaffold(
        backHandler = { onNavigationRequested(UserScreenContract.Effect.Navigation.NavigateBack) },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            RoarExtendedFloatingActionButton(
                icon = Icons.Filled.Edit,
                contentDescription = stringResource(id = R.string.edit_profile),
                text = stringResource(id = R.string.edit),
                onClick = { onEventSent(UserScreenContract.Event.OnEditAccountClick) }
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
                            top = 8.dp
                        )
                ) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        AutoResizeText(
                            text = "Hey, ${user.name}",
                            maxLines = 2,
                            style = MaterialTheme.typography.displayMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSizeRange = FontSizeRange(
                                min = 20.sp,
                                max = MaterialTheme.typography.displayMedium.fontSize,
                            ),
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(0.8f)
                        )

                        Icon(
                            Icons.Default.Person, contentDescription = null, modifier = Modifier
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
                        text = stringResource(id = R.string.backup),
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
                            Icon(Icons.Filled.Save, contentDescription = null)
                            Spacer(size = 6.dp)
                            Text(stringResource(id = R.string.export_button), style = MaterialTheme.typography.titleMedium)
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
                            Icon(Icons.Filled.Download, contentDescription = null)
                            Spacer(size = 6.dp)
                            Text(stringResource(id = R.string.import_button), style = MaterialTheme.typography.titleMedium)
                        }
                    }

                    Spacer(size = 32.dp)

                    Text(
                        text = stringResource(id = R.string.app_settings),
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(size = 8.dp)

                    DropdownMenu(
                        valueList = listOf("0", "1", "2", "3", "4", "5"),
                        listState = numberOfRemindersOnMainScreenState,
                        onChange = { onEventSent(UserScreenContract.Event.OnNumberOfRemindersOnMainScreen(it.toIntOrNull() ?: 2)) },
                        label = stringResource(id = R.string.number_of_reminders_main_screen),
                        leadingIcon = Icons.Filled.ListAlt,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(size = 8.dp)

                    LabelledCheckBox(
                        checked = state.dynamicColorActive,
                        onCheckedChange = {
                            onEventSent(UserScreenContract.Event.OnDynamicColorsStateChange(it))
                            activity.recreate()
                        },
                        label = stringResource(id = R.string.dynamic_color_switcher_title),
                        modifier = Modifier.fillMaxWidth(),
                        horizontalPadding = 8.dp
                    )

                    Spacer(size = 32.dp)

                    TextButton(
                        onClick = {
                            AlertDialog.Builder(context)
                                .setTitle(context.getString(R.string.logout_question))
                                .setMessage(context.getString(R.string.logout_description))
                                .setPositiveButton(context.getString(R.string.logout)) { dialog, _ ->
                                    dialog.dismiss()
                                    onEventSent(UserScreenContract.Event.OnLogout)
                                }
                                .setNegativeButton(context.getString(R.string.cancel)) { dialog, _ ->
                                    dialog.dismiss()
                                }
                                .create()
                                .show()
                        },
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        Text(
                            text = stringResource(id = R.string.logout),
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(horizontal = 32.dp, vertical = 8.dp)
                        )
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
        state = UserScreenContract.State(isLoading = false, dynamicColorActive = false, user = User("id", "Tester")),
        effectFlow = emptyFlow(),
        onEventSent = {},
        onNavigationRequested = {},
        backupFlow = flowOf()
    )
}