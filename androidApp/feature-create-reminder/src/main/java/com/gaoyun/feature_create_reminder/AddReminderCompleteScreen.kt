package com.gaoyun.feature_create_reminder

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gaoyun.common.R
import com.gaoyun.common.composables.*
import com.gaoyun.common.ext.getDrawableByName
import com.gaoyun.common.theme.RoarTheme
import com.gaoyun.roar.presentation.LAUNCH_LISTEN_FOR_EFFECTS
import com.gaoyun.roar.presentation.NavigationSideEffect
import com.gaoyun.roar.presentation.add_reminder.complete.AddReminderCompleteScreenContract
import com.gaoyun.roar.presentation.add_reminder.complete.AddReminderCompleteScreenViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.compose.getViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddReminderCompleteDestination(
    onNavigationCall: (NavigationSideEffect) -> Unit,
    petAvatar: String
) {
    val viewModel: AddReminderCompleteScreenViewModel = getViewModel()

    LaunchedEffect(LAUNCH_LISTEN_FOR_EFFECTS) {
        viewModel.effect.onEach { effect ->
            when (effect) {
                is AddReminderCompleteScreenContract.Effect.Navigation -> onNavigationCall(effect)
                else -> {}
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
    val context = LocalContext.current

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {

        Image(
            painter = painterResource(id = context.getDrawableByName(petAvatar)),
            contentDescription = stringResource(id = R.string.cd_avatar),
            modifier = Modifier.size(96.dp)
        )

        Spacer(16.dp)

        Text(stringResource(id = R.string.reminder_ready), style = MaterialTheme.typography.displayMedium)

        Spacer(16.dp)

        PrimaryElevatedButton(text = stringResource(id = R.string.continue_label), onClick = onContinueButtonClicked)
    }

}

@Composable
@Preview
fun AddPetSetupScreenPreview() {
    RoarTheme {
        ReminderAddingComplete("ic_cat_1") {}
    }
}