package com.gaoyun.roar.ui.features.add_pet.pet_data

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cake
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.gaoyun.roar.ui.common.composables.ReadonlyTextField
import com.gaoyun.roar.ui.common.dialog.DatePicker
import com.gaoyun.roar.util.DateFormats
import com.gaoyun.roar.util.formatDate
import com.gaoyun.roar.util.toLocalDate
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import org.jetbrains.compose.resources.stringResource
import roar.sharedlib.generated.resources.Res
import roar.sharedlib.generated.resources.birthday
import roar.sharedlib.generated.resources.pets_birthday

@Composable
fun PetDataScreenBirthdayPicker(
    petBirthdayState: MutableState<Long?>
) {
    var petBirthdayStringState by remember {
        mutableStateOf(TextFieldValue(petBirthdayState.value?.let {
            Instant.fromEpochMilliseconds(it)
                .toLocalDate()
                .formatDate(DateFormats.ddMmmmYyyyDateFormat)
        } ?: ""))
    }

    val showPicker = remember { mutableStateOf(false) }

    ReadonlyTextField(
        value = petBirthdayStringState,
        onValueChange = { petBirthdayStringState = it },
        leadingIcon = {
            Icon(
                Icons.Filled.Cake,
                stringResource(resource = Res.string.birthday),
                tint = MaterialTheme.colorScheme.onBackground
            )
        },
        label = {
            Text(text = stringResource(resource = Res.string.birthday))
        },
        modifier = Modifier.padding(horizontal = 24.dp),
        onClick = { showPicker.value = true },
    )

    if (showPicker.value) {
        DatePicker.pickDate(
            title = stringResource(Res.string.pets_birthday),
            end = Clock.System.now(),
            selectedDateMillis = petBirthdayState.value,
            onDatePicked = {
                petBirthdayState.value = it
                petBirthdayStringState = TextFieldValue(
                    Instant.fromEpochMilliseconds(it)
                        .toLocalDate()
                        .formatDate(DateFormats.ddMmmmYyyyDateFormat)
                )
                showPicker.value = false
            },
            onDismiss = { showPicker.value = false }
        )
    }
}