package com.gaoyun.feature_user_screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.compose.getViewModel

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
    )

}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun UserScreen(
    state: UserScreenContract.State,
    effectFlow: Flow<UserScreenContract.Effect>,
    onEventSent: (event: UserScreenContract.Event) -> Unit,
    onNavigationRequested: (navigationEffect: UserScreenContract.Effect.Navigation) -> Unit,
) {

    LaunchedEffect(LAUNCH_LISTEN_FOR_EFFECTS) {
        effectFlow.onEach { effect ->
            when (effect) {
                is UserScreenContract.Effect.Navigation -> onNavigationRequested(effect)
                else -> {}
            }
        }.collect()
    }

    SurfaceScaffold(
        floatingActionButton = {
            RoarExtendedFloatingActionButton(
                icon = Icons.Filled.Edit,
                contentDescription = "Edit user",
                text = "Edit",
                onClick = { }
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

                        Icon(Icons.Default.Person, contentDescription = "", modifier = Modifier
                            .size(40.dp)
                            .padding(end = 8.dp, top = 8.dp))
                    }

                    Spacer(size = 8.dp)

                    Text(
                        text = "Thank you for being with us",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
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
        onNavigationRequested = {}
    )
}