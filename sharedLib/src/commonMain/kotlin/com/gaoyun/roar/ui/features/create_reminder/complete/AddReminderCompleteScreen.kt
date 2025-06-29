package com.gaoyun.roar.ui.features.create_reminder.complete

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.gaoyun.roar.presentation.add_reminder.complete.AddReminderCompleteScreenContract
import com.gaoyun.roar.presentation.add_reminder.complete.AddReminderCompleteScreenViewModel
import com.gaoyun.roar.ui.common.composables.PrimaryElevatedButton
import com.gaoyun.roar.ui.common.composables.Spacer
import com.gaoyun.roar.ui.common.composables.SurfaceScaffold
import com.gaoyun.roar.ui.common.ext.getDrawableByName
import com.gaoyun.roar.ui.navigation.NavigationSideEffect
import com.gaoyun.roar.ui.theme.RoarThemePreview
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import roar.sharedlib.generated.resources.Res
import roar.sharedlib.generated.resources.cd_avatar
import roar.sharedlib.generated.resources.continue_label
import roar.sharedlib.generated.resources.reminder_ready

@Composable
fun AddReminderCompleteDestination(
    navigate: (NavigationSideEffect) -> Unit,
    petAvatar: String
) {
    val viewModel = koinViewModel<AddReminderCompleteScreenViewModel>()

    SurfaceScaffold {
        ReminderAddingComplete(
            petAvatar = petAvatar,
            onContinueButtonClicked = {
                navigate(AddReminderCompleteScreenContract.Effect.Navigation.Continue)
            }
        )
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

@Composable
@Preview
fun AddPetSetupScreenPreview() {
    RoarThemePreview {
        ReminderAddingComplete("ic_cat_1") {}
    }
}