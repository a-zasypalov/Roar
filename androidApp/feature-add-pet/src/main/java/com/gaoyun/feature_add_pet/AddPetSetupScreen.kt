package com.gaoyun.feature_add_pet

import androidx.activity.compose.BackHandler
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import com.gaoyun.common.OnLifecycleEvent
import com.gaoyun.common.R
import com.gaoyun.common.composables.*
import com.gaoyun.common.ext.getDrawableByName
import com.gaoyun.common.theme.RoarTheme
import com.gaoyun.roar.model.domain.Gender
import com.gaoyun.roar.model.domain.Pet
import com.gaoyun.roar.model.domain.PetType
import com.gaoyun.roar.presentation.LAUNCH_LISTEN_FOR_EFFECTS
import com.gaoyun.roar.presentation.NavigationSideEffect
import com.gaoyun.roar.presentation.add_pet.setup.AddPetSetupScreenContract
import com.gaoyun.roar.presentation.add_pet.setup.AddPetSetupScreenViewModel
import com.gaoyun.roar.util.randomUUID
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.datetime.LocalDate
import org.koin.androidx.compose.getViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPetSetupDestination(
    onNavigationCall: (NavigationSideEffect) -> Unit,
    petId: String,
) {
    val viewModel: AddPetSetupScreenViewModel = getViewModel()
    val state = viewModel.viewState.collectAsState().value

    OnLifecycleEvent { _, event ->
        if (event == Lifecycle.Event.ON_RESUME) {
            viewModel.setEvent(AddPetSetupScreenContract.Event.PetInit(petId))
        }
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
        BoxWithLoader(isLoading = state.isLoading) {
            state.pet?.let {
                PetAddingComplete(
                    pet = it,
                    onContinueButtonClicked = viewModel::setEvent
                )
            }
        }
    }
}

@Composable
private fun PetAddingComplete(
    pet: Pet,
    onContinueButtonClicked: (AddPetSetupScreenContract.Event.ContinueButtonClicked) -> Unit
) {
    val context = LocalContext.current

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {

        Image(
            painter = painterResource(id = context.getDrawableByName(pet.avatar)),
            contentDescription = pet.name,
            modifier = Modifier.size(96.dp)
        )

        Spacer(16.dp)

        Text(stringResource(id = R.string.new_pet_added), style = MaterialTheme.typography.displayMedium)

        Spacer(16.dp)

        PrimaryElevatedButton(text = stringResource(id = R.string.continue_label), onClick = {
            onContinueButtonClicked(AddPetSetupScreenContract.Event.ContinueButtonClicked)
        })
    }

}

@Composable
@Preview
fun AddPetSetupScreenPreview() {
    RoarTheme {
        PetAddingComplete(
            Pet(
                randomUUID(),
                PetType.CAT,
                "British",
                "Cat",
                "ic_cat_1",
                "1",
                LocalDate.fromEpochDays(1),
                false,
                Gender.MALE,
                "",
                LocalDate.fromEpochDays(1)
            )
        ) {}
    }
}