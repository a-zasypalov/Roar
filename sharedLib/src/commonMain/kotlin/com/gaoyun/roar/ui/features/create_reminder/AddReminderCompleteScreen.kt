package com.gaoyun.roar.ui.features.create_reminder

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.gaoyun.roar.presentation.LAUNCH_LISTEN_FOR_EFFECTS
import com.gaoyun.roar.presentation.NavigationSideEffect
import com.gaoyun.roar.presentation.add_reminder.complete.AddReminderCompleteScreenContract
import com.gaoyun.roar.presentation.add_reminder.complete.AddReminderCompleteScreenViewModel
import com.gaoyun.roar.ui.PrimaryElevatedButton
import com.gaoyun.roar.ui.Spacer
import com.gaoyun.roar.ui.SurfaceScaffold
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import moe.tlaster.precompose.koin.koinViewModel
import moe.tlaster.precompose.navigation.BackHandler

@Composable
fun AddReminderCompleteDestination(
    onNavigationCall: (NavigationSideEffect) -> Unit,
    petAvatar: String
) {
    val viewModel = koinViewModel(vmClass = AddReminderCompleteScreenViewModel::class)

    LaunchedEffect(LAUNCH_LISTEN_FOR_EFFECTS) {
        viewModel.effect.onEach { effect ->
            when (effect) {
                is AddReminderCompleteScreenContract.Effect.Navigation -> onNavigationCall(effect)
            }
        }.collect()
    }

    BackHandler {}

    SurfaceScaffold {
        ReminderAddingComplete(petAvatar) { viewModel.setEvent(AddReminderCompleteScreenContract.Event.ContinueButtonClicked) }
    }

}

@Composable
private fun ReminderAddingComplete(
    petAvatar: String,
    onContinueButtonClicked: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {

//        Image(
//            painter = painterResource(id = context.getDrawableByName(petAvatar)),
//            contentDescription = stringResource(id = R.string.cd_avatar),
//            modifier = Modifier.size(96.dp)
//        )

        Spacer(16.dp)

        Text(
            "Reminder ready", //stringResource(id = R.string.reminder_ready),
            style = MaterialTheme.typography.displayMedium
        )

        Spacer(16.dp)

        PrimaryElevatedButton(
            text = "Continue", //stringResource(id = R.string.continue_label),
            onClick = onContinueButtonClicked
        )
    }

}

//@Composable
//@Preview
//fun AddPetSetupScreenPreview() {
//    RoarThemePreview {
//        ReminderAddingComplete("ic_cat_1") {}
//    }
//}