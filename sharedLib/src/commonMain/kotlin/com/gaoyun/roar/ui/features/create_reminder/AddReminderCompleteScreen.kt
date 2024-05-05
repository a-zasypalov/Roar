package com.gaoyun.roar.ui.features.create_reminder

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
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
import com.gaoyun.roar.ui.common.composables.PrimaryElevatedButton
import com.gaoyun.roar.ui.common.composables.Spacer
import com.gaoyun.roar.ui.common.composables.SurfaceScaffold
import com.gaoyun.roar.ui.common.ext.getDrawableByName
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import moe.tlaster.precompose.koin.koinViewModel
import moe.tlaster.precompose.navigation.BackHandler
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import roar.sharedlib.generated.resources.Res
import roar.sharedlib.generated.resources.cd_avatar
import roar.sharedlib.generated.resources.continue_label
import roar.sharedlib.generated.resources.reminder_ready

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

        Image(
            painter = painterResource(getDrawableByName(petAvatar)),
            contentDescription = stringResource(Res.string.cd_avatar),
            modifier = Modifier.size(96.dp)
        )

        Spacer(16.dp)

        Text(
            stringResource(resource = Res.string.reminder_ready),
            style = MaterialTheme.typography.displayMedium
        )

        Spacer(16.dp)

        PrimaryElevatedButton(
            text = stringResource(resource = Res.string.continue_label),
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