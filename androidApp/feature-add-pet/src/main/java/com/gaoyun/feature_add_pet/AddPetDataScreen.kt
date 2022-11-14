package com.gaoyun.feature_add_pet

import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavHostController
import com.gaoyun.common.NavigationKeys
import com.gaoyun.common.OnLifecycleEvent
import com.gaoyun.common.theme.RoarTheme
import com.gaoyun.common.ui.*
import com.gaoyun.roar.presentation.LAUNCH_LISTEN_FOR_EFFECTS
import com.gaoyun.roar.presentation.add_pet.data.AddPetDataScreenContract
import com.gaoyun.roar.presentation.add_pet.data.AddPetDataScreenViewModel
import com.gaoyun.roar.util.DatetimeConstants
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.datetime.LocalDate
import org.koin.androidx.compose.getViewModel
import java.time.Instant
import java.time.ZoneId
import java.util.concurrent.TimeUnit


@Composable
fun AddPetDataDestination(navHostController: NavHostController, petType: String, avatar: String) {
    val viewModel: AddPetDataScreenViewModel = getViewModel()
    val state = viewModel.viewState.collectAsState().value

    AddPetDataScreen(
        state = state,
        effectFlow = viewModel.effect,
        onEventSent = { event -> viewModel.setEvent(event) },
        onNavigationRequested = { navigationEffect ->
            when (navigationEffect) {
                is AddPetDataScreenContract.Effect.Navigation.ToPetSetup -> navHostController.navigate(
                    route = "${NavigationKeys.RouteGlobal.ADD_PET_SETUP}/${navigationEffect.petId}"
                )
                is AddPetDataScreenContract.Effect.Navigation.NavigateBack -> navHostController.navigateUp()
            }
        },
        petType = petType,
        avatar = avatar
    )

    OnLifecycleEvent { _, event ->
        if (event == Lifecycle.Event.ON_RESUME) {
            viewModel.setEvent(AddPetDataScreenContract.Event.PetDataInit(petType, avatar))
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun AddPetDataScreen(
    state: AddPetDataScreenContract.State,
    effectFlow: Flow<AddPetDataScreenContract.Effect>,
    onEventSent: (event: AddPetDataScreenContract.Event) -> Unit,
    onNavigationRequested: (navigationEffect: AddPetDataScreenContract.Effect.Navigation) -> Unit,
    petType: String,
    avatar: String
) {
    LaunchedEffect(LAUNCH_LISTEN_FOR_EFFECTS) {
        effectFlow.onEach { effect ->
            when (effect) {
                is AddPetDataScreenContract.Effect.PetAdded -> {
                    onNavigationRequested(AddPetDataScreenContract.Effect.Navigation.ToPetSetup(effect.petId))
                }
                else -> {}
            }
        }.collect()
    }

    SurfaceScaffold {
        AddPetForm(
            petBreeds = state.breeds,
            onRegisterClick = { breed, name, birthday, isSterilized, gender, chipNumber ->
                onEventSent(
                    AddPetDataScreenContract.Event.AddPetButtonClicked(
                        petType = petType,
                        breed = breed,
                        name = name,
                        avatar = avatar,
                        birthday = birthday,
                        isSterilized = isSterilized,
                        gender = gender,
                        chipNumber = chipNumber
                    )
                )
            },
            avatar = avatar
        )
    }
}

@Composable
private fun AddPetForm(
    avatar: String,
    petBreeds: List<String>,
    onRegisterClick: (String, String, LocalDate, Boolean, String, String) -> Unit,
) {
    val activity = LocalContext.current as AppCompatActivity

    val petName = rememberSaveable { mutableStateOf("") }
    val chipNumberState = rememberSaveable { mutableStateOf("") }

    val petBreedState = rememberSaveable { mutableStateOf(petBreeds.firstOrNull() ?: "") }
    val petGenderState = rememberSaveable { mutableStateOf("Male") }

    val petBirthdayState = rememberSaveable { mutableStateOf<Long?>(null) }
    val petBirthdayStringState = remember { mutableStateOf(TextFieldValue()) }

    val petIsSterilizedState = rememberSaveable { mutableStateOf(false) }


    if (petBreedState.value.isEmpty() && petBreeds.isNotEmpty()) {
        petBreedState.value = petBreeds.first()
    }

    Box(
        contentAlignment = Alignment.BottomCenter,
        modifier = Modifier
            .fillMaxSize()
    ) {
        Text(
            text = "Pet's Card",
            style = MaterialTheme.typography.displayMedium,
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 10.dp, top = 32.dp, bottom = 16.dp),
        )
        SurfaceCard(
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp, bottomStart = 0.dp, bottomEnd = 0.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            modifier = Modifier.padding(horizontal = 12.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 32.dp, start = 24.dp, end = 24.dp)
                ) {
                    Image(
                        painter = painterResource(id = activity.getDrawableByName(avatar)),
                        contentDescription = "pet",
                        modifier = Modifier
                            .size(64.dp)
                            .padding(end = 12.dp)
                    )
                    TextFormField(
                        text = petName.value,
                        leadingIcon = {
                            Icon(
                                Icons.Filled.Pets,
                                "Name",
                                tint = MaterialTheme.colorScheme.onBackground
                            )
                        },
                        label = "Name",
                        onChange = {
                            petName.value = it
                        },
                        imeAction = ImeAction.Done,
                    )
                }

                Spacer(size = 16.dp)

                DropdownMenu(
                    valueList = petBreeds,
                    listState = petBreedState,
                    label = "Breed",
                    leadingIcon = Icons.Filled.List,
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp)
                )

                Spacer(size = 16.dp)

                DropdownMenu(
                    valueList = listOf("Male", "Female"),
                    listState = petGenderState,
                    label = "Gender",
                    leadingIcon = if (petGenderState.value == "Male") Icons.Filled.Male else Icons.Filled.Female,
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp)
                )

                Spacer(size = 16.dp)

                ReadonlyTextField(
                    value = petBirthdayStringState.value,
                    onValueChange = { petBirthdayStringState.value = it },
                    leadingIcon = {
                        Icon(
                            Icons.Filled.Cake,
                            "Birthday",
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    },
                    label = {
                        Text(text = "Birthday")
                    },
                    modifier = Modifier.padding(horizontal = 24.dp),
                    onClick = {
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
                        } else {
                            dialogBuilder.setSelection(System.currentTimeMillis())
                        }
                        val dialog = dialogBuilder.build()
                        dialog.addOnPositiveButtonClickListener {
                            petBirthdayState.value = it
                            petBirthdayStringState.value = TextFieldValue(
                                Instant.ofEpochMilli(it)
                                    .atZone(ZoneId.systemDefault())
                                    .toLocalDate()
                                    .toString()
                            )
                        }
                        dialog.show(activity.supportFragmentManager, "TAG")
                    },
                )

                Spacer(size = 16.dp)

                TextFormField(
                    text = chipNumberState.value,
                    leadingIcon = {
                        Icon(
                            Icons.Filled.Memory,
                            "Chip Number",
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    },
                    label = "Chip Number",
                    onChange = {
                        chipNumberState.value = it
                    },
                    imeAction = ImeAction.Done,
                    modifier = Modifier.padding(horizontal = 24.dp),
                )

                Spacer(size = 16.dp)

                LabelledCheckBox(
                    checked = petIsSterilizedState.value,
                    onCheckedChange = { petIsSterilizedState.value = it },
                    label = "Pet is sterilized",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp)
                )

                Spacer(size = 32.dp)

                PrimaryElevatedButtonOnSurface(
                    text = "Add pet",
                    onClick = {
                        onRegisterClick(
                            petBreedState.value,
                            petName.value,
                            LocalDate.fromEpochDays(TimeUnit.MILLISECONDS.toDays(petBirthdayState.value ?: System.currentTimeMillis()).toInt()),
                            petIsSterilizedState.value,
                            petGenderState.value,
                            chipNumberState.value
                        )
                    },
                )

                Spacer(size = 32.dp)

            }
        }
    }
}

@Composable
@Preview
fun AddPetScreenPreview() {
    RoarTheme {
        AddPetForm("ic_cat_15", listOf()) { _, _, _, _, _, _ -> }
    }
}