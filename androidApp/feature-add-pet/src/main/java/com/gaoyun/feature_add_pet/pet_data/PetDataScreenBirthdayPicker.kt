package com.gaoyun.feature_add_pet.pet_data

import androidx.appcompat.app.AppCompatActivity
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.gaoyun.common.DateUtils.ddMmmmYyyyDateFormatter
import com.gaoyun.common.R
import com.gaoyun.roar.ui.common.composables.ReadonlyTextField
import com.gaoyun.common.dialog.DatePicker
import java.time.Instant
import java.time.ZoneId

@Composable
fun PetDataScreenBirthdayPicker(
    petBirthdayState: MutableState<Long?>
) {
    val activity = LocalContext.current as AppCompatActivity
    var petBirthdayStringState by remember {
        mutableStateOf(TextFieldValue(petBirthdayState.value?.let {
            Instant.ofEpochMilli(it)
                .atZone(ZoneId.systemDefault())
                .toLocalDate()
                .format(ddMmmmYyyyDateFormatter)
        } ?: ""))
    }

    ReadonlyTextField(
        value = petBirthdayStringState,
        onValueChange = { petBirthdayStringState = it },
        leadingIcon = {
            Icon(
                Icons.Filled.Cake,
                stringResource(id = R.string.birthday),
                tint = MaterialTheme.colorScheme.onBackground
            )
        },
        label = {
            Text(text = stringResource(id = R.string.birthday))
        },
        modifier = Modifier.padding(horizontal = 24.dp),
        onClick = {
            DatePicker.pickDate(
                title = activity.getString(R.string.pets_birthday),
                end = Instant.now().toEpochMilli(),
                fragmentManager = activity.supportFragmentManager,
                selectedDateMillis = petBirthdayState.value,
                onDatePicked = {
                    petBirthdayState.value = it
                    petBirthdayStringState = TextFieldValue(
                        Instant.ofEpochMilli(it)
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate()
                            .format(ddMmmmYyyyDateFormatter)
                    )
                }
            )
        },
    )
}