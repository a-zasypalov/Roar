package com.gaoyun.feature_add_pet.pet_data

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gaoyun.common.R
import com.gaoyun.common.composables.DropdownMenu
import com.gaoyun.common.composables.LabelledCheckBox
import com.gaoyun.roar.ui.PrimaryElevatedButtonOnSurface
import com.gaoyun.roar.ui.Spacer
import com.gaoyun.common.composables.SurfaceCard
import com.gaoyun.common.composables.TextFormField
import com.gaoyun.common.composables.surfaceCardFormElevation
import com.gaoyun.common.composables.surfaceCardFormShape
import com.gaoyun.common.ext.toLocalizedStringId
import com.gaoyun.roar.model.domain.Gender
import com.gaoyun.roar.model.domain.Pet
import com.gaoyun.roar.model.domain.toGender
import com.gaoyun.roar.presentation.add_pet.data.AddPetDataScreenContract
import com.gaoyun.roar.ui.theme.RoarThemePreview
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import java.util.concurrent.TimeUnit

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
        remember { mutableStateOf(petToEdit?.breed ?: petBreeds.firstOrNull() ?: "") }
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

    if (petBreedState.value.isEmpty() && petBreeds.isNotEmpty()) {
        petBreedState.value = petBreeds.first()
    }

    Box(
        contentAlignment = Alignment.BottomCenter,
        modifier = Modifier
            .fillMaxSize()
    ) {
        Text(
            text = stringResource(id = R.string.pets_card),
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

                DropdownMenu(
                    valueList = petBreeds,
                    listState = petBreedState,
                    valueDisplayList = null,
                    listDisplayState = null,
                    label = stringResource(id = R.string.breed),
                    leadingIcon = Icons.AutoMirrored.Filled.List,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                )

                Spacer(size = 16.dp)

                DropdownMenu(
                    valueList = Gender.GENDER_LIST,
                    listState = petGenderState,
                    valueDisplayList = Gender.GENDER_LIST.map {
                        it.toGender().toLocalizedStringId()
                    },
                    listDisplayState = petGenderState.value.toGender().toLocalizedStringId(),
                    label = stringResource(id = R.string.gender),
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
                            stringResource(id = R.string.chip_number),
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    },
                    label = stringResource(id = R.string.chip_number),
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
                    label = stringResource(id = R.string.pet_is_sterilized),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp)
                )

                Spacer(size = 32.dp)

                PrimaryElevatedButtonOnSurface(
                    text = if (petToEdit != null) stringResource(id = R.string.save) else stringResource(
                        id = R.string.add_pet
                    ),
                    onClick = {
                        if (petName.value.isBlank()) {
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar("Name shouldn't be empty")
                            }
                        } else if (petBirthdayState.value?.let { it < System.currentTimeMillis() } != true) {
                            coroutineScope.launch { snackbarHostState.showSnackbar("Birth date isn't correct") }
                        } else {
                            onRegisterClick(
                                AddPetDataScreenContract.Event.AddPetButtonClicked(
                                    petType = petType,
                                    breed = petBreedState.value,
                                    name = petName.value,
                                    avatar = avatar,
                                    birthday = LocalDate.fromEpochDays(
                                        TimeUnit.MILLISECONDS.toDays(
                                            petBirthdayState.value ?: System.currentTimeMillis()
                                        ).toInt()
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