package com.gaoyun.roar.ui.features.add_pet

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gaoyun.roar.model.domain.Pet
import com.gaoyun.roar.presentation.LAUNCH_LISTEN_FOR_EFFECTS
import com.gaoyun.roar.presentation.NavigationSideEffect
import com.gaoyun.roar.presentation.add_pet.setup.AddPetSetupScreenContract
import com.gaoyun.roar.presentation.add_pet.setup.AddPetSetupScreenViewModel
import com.gaoyun.roar.ui.common.composables.PrimaryElevatedButton
import com.gaoyun.roar.ui.common.composables.Spacer
import com.gaoyun.roar.ui.common.composables.SurfaceScaffold
import com.gaoyun.roar.ui.common.composables.BoxWithLoader
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import moe.tlaster.precompose.koin.koinViewModel
import moe.tlaster.precompose.navigation.BackHandler
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource
import roar.sharedlib.generated.resources.Res
import roar.sharedlib.generated.resources.new_pet_added
import roar.sharedlib.generated.resources.new_pet_added_subtitle
import roar.sharedlib.generated.resources.open_reminders_menu
import roar.sharedlib.generated.resources.skip_label

@Composable
fun AddPetSetupDestination(
    onNavigationCall: (NavigationSideEffect) -> Unit,
    petId: String,
) {
    val viewModel = koinViewModel(vmClass = AddPetSetupScreenViewModel::class)
    val state = viewModel.viewState.collectAsState().value

    LaunchedEffect(Unit) {
        //TODO: Was on onCreate
        viewModel.setEvent(AddPetSetupScreenContract.Event.PetInit(petId))
    }

    LaunchedEffect(LAUNCH_LISTEN_FOR_EFFECTS) {
        viewModel.effect.onEach { effect ->
            when (effect) {
                is AddPetSetupScreenContract.Effect.Navigation -> onNavigationCall(effect)
            }
        }.collect()
    }

    BackHandler(enabled = true) {}

    SurfaceScaffold {
        BoxWithLoader(isLoading = state.isLoading, modifier = Modifier.fillMaxSize()) {
            state.pet?.let {
                PetAddingComplete(
                    pet = it,
                    onContinueButtonClicked = viewModel::setEvent,
                    onAddReminderButtonClicked = viewModel::setEvent,
                )
            }
        }
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
private fun PetAddingComplete(
    pet: Pet,
    onContinueButtonClicked: (AddPetSetupScreenContract.Event.ContinueButtonClicked) -> Unit,
    onAddReminderButtonClicked: (AddPetSetupScreenContract.Event.OpenTemplatesButtonClicked) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding()
    ) {

//        Image(
//            painter = painterResource(id = context.getDrawableByName(pet.avatar)),
//            contentDescription = pet.name,
//            modifier = Modifier.size(96.dp)
//        )

        Spacer(16.dp)

        Text(
            stringResource(resource = Res.string.new_pet_added),
            style = MaterialTheme.typography.displayMedium,
            modifier = Modifier.padding(horizontal = 8.dp)
        )

        Spacer(8.dp)

        Text(
            text = stringResource(resource = Res.string.new_pet_added_subtitle, pet.name),
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Normal),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 8.dp)
        )
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(end = 16.dp, top = 4.dp)
    ) {
        TextButton(
            onClick = {
                onContinueButtonClicked(AddPetSetupScreenContract.Event.ContinueButtonClicked)
            },
            modifier = Modifier.align(Alignment.TopEnd)
        ) {
            Text(
                stringResource(resource = Res.string.skip_label),
                modifier = Modifier.padding(vertical = 8.dp),
                fontSize = 16.sp,
            )
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding()
    ) {
        PrimaryElevatedButton(
            text = stringResource(resource = Res.string.open_reminders_menu),
            onClick = {
                onAddReminderButtonClicked(AddPetSetupScreenContract.Event.OpenTemplatesButtonClicked)
            },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 24.dp)
        )
    }
}

//@Composable
//@Preview
//fun AddPetSetupScreenPreview() {
//    RoarThemePreview {
//        Box {
//            PetAddingComplete(
//                Pet(
//                    randomUUID(),
//                    PetType.CAT,
//                    "British",
//                    "Cat",
//                    "ic_cat_1",
//                    "1",
//                    LocalDate.fromEpochDays(1),
//                    false,
//                    Gender.MALE,
//                    "",
//                    LocalDate.fromEpochDays(1)
//                ),
//                {}, {}
//            )
//        }
//    }
//}