package com.gaoyun.roar.ui.features.add_pet.pet_data

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Female
import androidx.compose.material.icons.filled.Male
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.unit.dp
import com.gaoyun.roar.model.domain.Gender
import com.gaoyun.roar.model.domain.Pet
import com.gaoyun.roar.model.domain.toGender
import com.gaoyun.roar.presentation.add_pet.data.AddPetDataScreenContract
import com.gaoyun.roar.ui.common.composables.DropdownMenu
import com.gaoyun.roar.ui.common.composables.LabelledCheckBox
import com.gaoyun.roar.ui.common.composables.PrimaryElevatedButtonOnSurface
import com.gaoyun.roar.ui.common.composables.ReadonlyTextField
import com.gaoyun.roar.ui.common.composables.Spacer
import com.gaoyun.roar.ui.common.composables.SurfaceCard
import com.gaoyun.roar.ui.common.composables.TextFormField
import com.gaoyun.roar.ui.common.composables.surfaceCardFormElevation
import com.gaoyun.roar.ui.common.composables.surfaceCardFormShape
import com.gaoyun.roar.ui.common.toLocalizedStringId
import com.gaoyun.roar.ui.theme.RoarThemePreview
import com.gaoyun.roar.util.toLocalDate
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import roar.sharedlib.generated.resources.Res
import roar.sharedlib.generated.resources.add_pet
import roar.sharedlib.generated.resources.breed
import roar.sharedlib.generated.resources.chip_number
import roar.sharedlib.generated.resources.gender
import roar.sharedlib.generated.resources.pet_is_sterilized
import roar.sharedlib.generated.resources.pets_card
import roar.sharedlib.generated.resources.save
import kotlin.time.Duration.Companion.milliseconds

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AddPetForm(
    avatar: String,
    petBreeds: List<String>,
    petType: String,
    petToEdit: Pet?,
    snackbarHostState: SnackbarHostState,
    onRegisterClick: (AddPetDataScreenContract.Event.AddPetButtonClicked) -> Unit,
    onAvatarEditClick: (AddPetDataScreenContract.Event.NavigateToAvatarEdit) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()

    val petName = remember { mutableStateOf(petToEdit?.name ?: "") }
    val petBreedState =
        remember { mutableStateOf(TextFieldValue(petToEdit?.breed ?: petBreeds.firstOrNull() ?: "")) }
    val petGenderState = remember {
        mutableStateOf(
            petToEdit?.gender?.toString()?.capitalize(Locale.current) ?: Gender.MALE_STRING
        )
    }
    val petBirthdayState = remember {
        mutableStateOf(
            petToEdit?.birthday?.atStartOfDayIn(TimeZone.currentSystemDefault())
                ?.toEpochMilliseconds()
        )
    }
    var chipNumberState by remember { mutableStateOf(petToEdit?.chipNumber ?: "") }
    var petIsSterilizedState by remember { mutableStateOf(petToEdit?.isSterilized ?: false) }

    if (petBreedState.value.text.isEmpty() && petBreeds.isNotEmpty()) {
        petBreedState.value = TextFieldValue(petBreeds.first())
    }

    var showPickerDialog by remember { mutableStateOf(false) }

    Box(
        contentAlignment = Alignment.BottomCenter,
        modifier = Modifier
            .fillMaxSize()
    ) {
        if (showPickerDialog) {
            BasicAlertDialog(
                onDismissRequest = { showPickerDialog = false },
                modifier = Modifier.padding(vertical = 16.dp)
                    .background(
                        color = MaterialTheme.colorScheme.surface,
                        shape = MaterialTheme.shapes.small
                    ),
            ) {
                SearchablePicker(
                    items = petBreeds,
                    onItemSelected = {
                        petBreedState.value = TextFieldValue(it)
                        showPickerDialog = false
                    })
            }
        }

        Text(
            text = stringResource(resource = Res.string.pets_card),
            style = MaterialTheme.typography.displayMedium,
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 10.dp, top = 8.dp, bottom = 16.dp),
        )
        SurfaceCard(
            shape = surfaceCardFormShape,
            elevation = surfaceCardFormElevation(),
            modifier = Modifier.padding(horizontal = 6.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .navigationBarsPadding()
            ) {
                PetDataFormHeader(avatar, petToEdit, petName, onAvatarEditClick)

                Spacer(size = 16.dp)

                ReadonlyTextField(
                    value = petBreedState.value,
                    onValueChange = { petBreedState.value = it },
                    leadingIcon = {
                        Icon(
                            Icons.AutoMirrored.Filled.List,
                            stringResource(resource = Res.string.breed),
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    },
                    label = {
                        Text(text = stringResource(resource = Res.string.breed))
                    },
                    modifier = Modifier.padding(horizontal = 24.dp),
                    onClick = { showPickerDialog = true },
                )

                Spacer(size = 16.dp)

                DropdownMenu(
                    valueList = Gender.GENDER_LIST,
                    listState = petGenderState,
                    valueDisplayList = Gender.GENDER_LIST.map {
                        it.toGender().toLocalizedStringId()
                    },
                    listDisplayState = petGenderState.value.toGender().toLocalizedStringId(),
                    label = stringResource(resource = Res.string.gender),
                    leadingIcon = if (petGenderState.value == Gender.MALE_STRING) Icons.Filled.Male else Icons.Filled.Female,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                )

                Spacer(size = 16.dp)

                PetDataScreenBirthdayPicker(petBirthdayState)

                Spacer(size = 16.dp)

                TextFormField(
                    text = chipNumberState,
                    leadingIcon = {
                        Icon(
                            Icons.Filled.Memory,
                            stringResource(resource = Res.string.chip_number),
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    },
                    label = stringResource(resource = Res.string.chip_number),
                    onChange = {
                        chipNumberState = it
                    },
                    imeAction = ImeAction.Done,
                    modifier = Modifier.padding(horizontal = 24.dp),
                )

                Spacer(size = 16.dp)

                LabelledCheckBox(
                    checked = petIsSterilizedState,
                    onCheckedChange = { petIsSterilizedState = it },
                    label = stringResource(resource = Res.string.pet_is_sterilized),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp)
                )

                Spacer(size = 32.dp)

                PrimaryElevatedButtonOnSurface(
                    text = if (petToEdit != null) stringResource(Res.string.save) else stringResource(Res.string.add_pet),
                    onClick = {
                        if (petName.value.isBlank()) {
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar("Name shouldn't be empty")
                            }
                        } else if (petBirthdayState.value?.let { it < Clock.System.now().toEpochMilliseconds() } != true) {
                            coroutineScope.launch { snackbarHostState.showSnackbar("Birth date isn't correct") }
                        } else {
                            onRegisterClick(
                                AddPetDataScreenContract.Event.AddPetButtonClicked(
                                    petType = petType,
                                    breed = petBreedState.value.text,
                                    name = petName.value,
                                    avatar = avatar,
                                    birthday = LocalDate.fromEpochDays(
                                        (petBirthdayState.value ?: Clock.System.now().toEpochMilliseconds())
                                        .milliseconds
                                        .inWholeDays
                                        .toInt()
                                    ),
                                    isSterilized = petIsSterilizedState,
                                    gender = petGenderState.value,
                                    chipNumber = chipNumberState
                                )
                            )
                        }
                    },
                )

                Spacer(size = 16.dp)

            }
        }
    }
}

@Composable
@Preview
fun AddPetScreenPreview() {
    RoarThemePreview {
        AddPetForm("ic_cat_15", listOf(), "cat", null, SnackbarHostState(), { }, { })
    }
}