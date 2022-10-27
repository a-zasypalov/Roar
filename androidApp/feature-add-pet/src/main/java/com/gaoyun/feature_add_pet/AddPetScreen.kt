package com.gaoyun.feature_add_pet

import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cake
import androidx.compose.material.icons.filled.Pets
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.gaoyun.common.theme.RoarTheme
import com.gaoyun.common.ui.LabelledCheckBox
import com.gaoyun.common.ui.PrimaryRaisedButton
import com.gaoyun.common.ui.TextFormField
import com.gaoyun.roar.presentation.LAUNCH_LISTEN_FOR_EFFECTS
import com.gaoyun.roar.presentation.add_pet.AddPetScreenContract
import com.gaoyun.roar.presentation.add_pet.AddPetScreenViewModel
import com.gaoyun.roar.util.DatetimeConstants
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.datetime.LocalDate
import org.koin.androidx.compose.getViewModel
import java.time.Instant

@Composable
fun AddPetDestination(navHostController: NavHostController) {
    val viewModel: AddPetScreenViewModel = getViewModel()
    val state = viewModel.viewState.collectAsState().value

    AddPetScreen(
        state = state,
        effectFlow = viewModel.effect,
        onEventSent = { event -> viewModel.setEvent(event) },
        onNavigationRequested = { navigationEffect ->
            when (navigationEffect) {
                is AddPetScreenContract.Effect.Navigation.NavigateBack -> navHostController.navigateUp()
            }
        },
    )

}

@Composable
fun AddPetScreen(
    state: AddPetScreenContract.State,
    effectFlow: Flow<AddPetScreenContract.Effect>,
    onEventSent: (event: AddPetScreenContract.Event) -> Unit,
    onNavigationRequested: (navigationEffect: AddPetScreenContract.Effect.Navigation) -> Unit,
) {
    LaunchedEffect(LAUNCH_LISTEN_FOR_EFFECTS) {
        effectFlow.onEach { effect ->
            when (effect) {
                is AddPetScreenContract.Effect.PetAdded -> {
                    onNavigationRequested(AddPetScreenContract.Effect.Navigation.NavigateBack)
                }
                else -> {}
            }
        }.collect()
    }

    val rememberedState = rememberScaffoldState()

    Scaffold(scaffoldState = rememberedState) {
        AddPetForm { petType, breed, name, birthday, isSterilized ->
            onEventSent(AddPetScreenContract.Event.AddPetButtonClicked(petType, breed, name, birthday, isSterilized))
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AddPetForm(
    onRegisterClick: (String, String, String, LocalDate, Boolean) -> Unit
) {
    val focusManager = LocalFocusManager.current
    val activity = LocalContext.current as AppCompatActivity

    val petName = rememberSaveable { mutableStateOf("") }

    val petTypes = listOf("Cat", "Dog")
    var petTypesExpanded by remember { mutableStateOf(false) }
    val petTypeState = rememberSaveable { mutableStateOf("") }

    val petBreeds = listOf("No breed", "Breed")
    var petBreedsExpanded by remember { mutableStateOf(false) }
    val petBreedState = rememberSaveable { mutableStateOf("") }

    val petBirthdayState = rememberSaveable { mutableStateOf<Long?>(null) }
    val petIsSterilizedState = rememberSaveable { mutableStateOf(false) }

    Box(contentAlignment = Alignment.BottomCenter) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxHeight()
                .verticalScroll(rememberScrollState())
                .navigationBarsPadding()
                .imePadding()
        ) {
            Text("New pet")

            Spacer(modifier = Modifier.size(16.dp))

            ExposedDropdownMenuBox(
                expanded = petTypesExpanded,
                onExpandedChange = { petTypesExpanded = !petTypesExpanded },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
            ) {
                TextField(
                    readOnly = true,
                    value = petTypeState.value,
                    onValueChange = { },
                    label = { Text("Pet") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = petTypesExpanded)
                    },
                    colors = ExposedDropdownMenuDefaults.textFieldColors(),
                    modifier = Modifier.fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = petTypesExpanded,
                    onDismissRequest = { petTypesExpanded = false }
                ) {
                    petTypes.forEach { petTypeSelection ->
                        DropdownMenuItem(
                            onClick = {
                                petTypeState.value = petTypeSelection
                                petTypesExpanded = false
                            }
                        ) {
                            Text(text = petTypeSelection)
                        }
                    }
                }
            }

            if (petTypeState.value.isNotEmpty()) {
                Spacer(modifier = Modifier.size(16.dp))

                ExposedDropdownMenuBox(
                    expanded = petBreedsExpanded,
                    onExpandedChange = { petBreedsExpanded = !petBreedsExpanded },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                ) {
                    TextField(
                        readOnly = true,
                        value = petBreedState.value,
                        onValueChange = { },
                        label = { Text("Breed") },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = petBreedsExpanded)
                        },
                        colors = ExposedDropdownMenuDefaults.textFieldColors(),
                        modifier = Modifier.fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = petBreedsExpanded,
                        onDismissRequest = { petBreedsExpanded = false }
                    ) {
                        petBreeds.forEach { breedSelection ->
                            DropdownMenuItem(
                                onClick = {
                                    petBreedState.value = breedSelection
                                    petBreedsExpanded = false
                                }
                            ) {
                                Text(text = breedSelection)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.size(16.dp))

            TextFormField(
                text = petName.value,
                leadingIcon = {
                    Icon(
                        Icons.Filled.Pets,
                        "Name",
                        tint = MaterialTheme.colors.onBackground
                    )
                },
                placeholder = "Name",
                onChange = {
                    petName.value = it
                },
                imeAction = ImeAction.Done,
                modifier = Modifier.padding(horizontal = 24.dp)
            )

            Spacer(modifier = Modifier.size(16.dp))

            TextFormField(
                text = petBirthdayState.value?.toString() ?: "",
                placeholder = "Birthday",
                leadingIcon = {
                    Icon(
                        Icons.Filled.Cake,
                        "Birthday",
                        tint = MaterialTheme.colors.onBackground
                    )
                },
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .clickable {
                        val dialogBuilder = MaterialDatePicker.Builder
                            .datePicker()
                            .setTitleText("Pet's birthday")
                            .setCalendarConstraints(
                                CalendarConstraints
                                    .Builder()
                                    .setStart(
                                        Instant
                                            .now()
                                            .minusMillis(30 * DatetimeConstants.YEAR_MILLIS)
                                            .toEpochMilli()
                                    )
                                    .setEnd(
                                        Instant
                                            .now()
                                            .toEpochMilli()
                                    )
                                    .build()
                            )
                        if (petBirthdayState.value != null) {
                            dialogBuilder.setSelection(petBirthdayState.value)
                        }
                        val dialog = dialogBuilder.build()
                        dialog.addOnPositiveButtonClickListener { petBirthdayState.value = it }
                        dialog.show(activity.supportFragmentManager, "TAG")
                    }
            )

            Spacer(modifier = Modifier.size(16.dp))

            LabelledCheckBox(
                checked = petIsSterilizedState.value,
                onCheckedChange = { petIsSterilizedState.value = it },
                label = "Pet is sterilized",
                modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp)
            )

            Spacer(modifier = Modifier.size(90.dp))

        }

        PrimaryRaisedButton(
            text = "Add pet",
            onClick = { },
            modifier = Modifier.padding(bottom = 32.dp)
        )
    }
}

@Composable
@Preview
fun AddPetScreenPreview() {
    RoarTheme {
        AddPetForm { _, _, _, _, _ -> }
    }
}