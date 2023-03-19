package com.gaoyun.feature_user_screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavHostController
import com.gaoyun.common.OnLifecycleEvent
import com.gaoyun.common.R
import com.gaoyun.common.ui.*
import com.gaoyun.roar.model.domain.User
import com.gaoyun.roar.presentation.LAUNCH_LISTEN_FOR_EFFECTS
import com.gaoyun.roar.presentation.user_edit.EditUserScreenContract
import com.gaoyun.roar.presentation.user_edit.EditUserScreenViewModel
import kotlinx.coroutines.flow.*
import org.koin.androidx.compose.getViewModel

@Composable
fun EditUserScreenDestination(
    navHostController: NavHostController,
) {
    val viewModel: EditUserScreenViewModel = getViewModel()
    val state = viewModel.viewState.collectAsState().value

    OnLifecycleEvent { _, event ->
        if (event == Lifecycle.Event.ON_CREATE) {
            viewModel.buildScreenState()
        }
    }

    EditUserScreen(
        state = state,
        effectFlow = viewModel.effect,
        onEventSent = { event -> viewModel.setEvent(event) },
        onNavigationRequested = { navigationEffect ->
            when (navigationEffect) {
                is EditUserScreenContract.Effect.Navigation.NavigateBack ->
                    navHostController.navigateUp()
            }
        },
    )

}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun EditUserScreen(
    state: EditUserScreenContract.State,
    effectFlow: Flow<EditUserScreenContract.Effect>,
    onEventSent: (event: EditUserScreenContract.Event) -> Unit,
    onNavigationRequested: (navigationEffect: EditUserScreenContract.Effect.Navigation) -> Unit,
) {
    LaunchedEffect(LAUNCH_LISTEN_FOR_EFFECTS) {
        effectFlow.onEach { effect ->
            when (effect) {
                is EditUserScreenContract.Effect.Navigation -> onNavigationRequested(effect)
                else -> {}
            }
        }.collect()
    }

    SurfaceScaffold(
        floatingActionButtonPosition = FabPosition.End,
        backHandler = { onNavigationRequested(EditUserScreenContract.Effect.Navigation.NavigateBack) },
    ) {
        BoxWithLoader(isLoading = state.userToEdit == null) {
            state.userToEdit?.let { user ->
                Box(
                    contentAlignment = Alignment.BottomCenter,
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    Text(
                        text = stringResource(id = R.string.edit_profile),
                        style = MaterialTheme.typography.displayMedium,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(start = 10.dp, top = 8.dp, bottom = 16.dp),
                    )
                    EditUserForm(user) { onEventSent(EditUserScreenContract.Event.OnSaveAccountClick(it)) }
                }
            }
        }
    }
}

@Composable
private fun EditUserForm(user: User, onSaveClick: (User) -> Unit) {
    val userName = remember { mutableStateOf(user.name) }

    SurfaceCard(
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp, bottomStart = 0.dp, bottomEnd = 0.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        modifier = Modifier.padding(horizontal = 6.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp)
        ) {
            Spacer(size = 32.dp)

            TextFormField(
                text = userName.value,
                leadingIcon = {
                    Icon(
                        Icons.Filled.Person,
                        stringResource(id = R.string.name),
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                },
                label = stringResource(id = R.string.name),
                onChange = {
                    userName.value = it
                },
                imeAction = ImeAction.Done,
            )

            Spacer(size = 32.dp)

            PrimaryElevatedButtonOnSurface(
                text = stringResource(id = R.string.save),
                onClick = { onSaveClick(user.copy(name = userName.value)) },
            )

            Spacer(size = 32.dp)
        }
    }
}

@Preview
@Composable
fun EditUserScreenPreview() {
    EditUserScreen(
        state = EditUserScreenContract.State(isLoading = false, userToEdit = User("id", "Tester")),
        effectFlow = emptyFlow(),
        onEventSent = {},
        onNavigationRequested = {},
    )
}