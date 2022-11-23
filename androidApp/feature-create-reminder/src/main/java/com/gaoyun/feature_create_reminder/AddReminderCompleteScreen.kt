package com.gaoyun.feature_create_reminder

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.gaoyun.common.NavigationKeys
import com.gaoyun.common.theme.RoarTheme
import com.gaoyun.common.ui.*
import com.gaoyun.roar.presentation.LAUNCH_LISTEN_FOR_EFFECTS
import com.gaoyun.roar.presentation.add_reminder.complete.AddReminderCompleteScreenContract
import com.gaoyun.roar.presentation.add_reminder.complete.AddReminderCompleteScreenViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.compose.getViewModel

@Composable
fun AddReminderCompleteDestination(navHostController: NavHostController, petAvatar: String) {
    val viewModel: AddReminderCompleteScreenViewModel = getViewModel()
    val state = viewModel.viewState.collectAsState().value

    AddReminderCompleteScreen(
        petAvatar = petAvatar,
        state = state,
        effectFlow = viewModel.effect,
        onEventSent = { event -> viewModel.setEvent(event) },
        onNavigationRequested = { navigationEffect ->
            when (navigationEffect) {
                is AddReminderCompleteScreenContract.Effect.Navigation.Continue ->
                    navHostController.popBackStack(NavigationKeys.Route.ADD_REMINDER_ROUTE, true)

                is AddReminderCompleteScreenContract.Effect.Navigation.NavigateBack ->
                    navHostController.navigateUp()
            }
        },
    )

}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun AddReminderCompleteScreen(
    petAvatar: String,
    state: AddReminderCompleteScreenContract.State,
    effectFlow: Flow<AddReminderCompleteScreenContract.Effect>,
    onEventSent: (event: AddReminderCompleteScreenContract.Event) -> Unit,
    onNavigationRequested: (navigationEffect: AddReminderCompleteScreenContract.Effect.Navigation) -> Unit,
) {
    LaunchedEffect(LAUNCH_LISTEN_FOR_EFFECTS) {
        effectFlow.onEach { effect ->
            when (effect) {
                is AddReminderCompleteScreenContract.Effect.Navigation -> onNavigationRequested(effect)
                else -> {}
            }
        }.collect()
    }

    SurfaceScaffold {
        ReminderAddingComplete(petAvatar) { onEventSent(AddReminderCompleteScreenContract.Event.ContinueButtonClicked) }
    }
}

@Composable
private fun ReminderAddingComplete(
    petAvatar: String,
    onContinueButtonClicked: () -> Unit
) {
    val context = LocalContext.current

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {

        Image(
            painter = painterResource(id = context.getDrawableByName(petAvatar)),
            contentDescription = "avatar",
            modifier = Modifier.size(96.dp)
        )

        Spacer(16.dp)

        Text("Reminder ready!", style = MaterialTheme.typography.displayMedium)

        Spacer(16.dp)

        PrimaryElevatedButton(text = "Continue", onClick = onContinueButtonClicked)
    }

}

@Composable
@Preview
fun AddPetSetupScreenPreview() {
    RoarTheme {
        ReminderAddingComplete("ic_cat_1") {}
    }
}