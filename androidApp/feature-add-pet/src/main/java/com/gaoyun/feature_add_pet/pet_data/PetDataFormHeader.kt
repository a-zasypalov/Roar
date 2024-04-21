package com.gaoyun.feature_add_pet.pet_data

import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.gaoyun.common.R
import com.gaoyun.roar.ui.common.composables.TextFormField
import com.gaoyun.common.ext.getDrawableByName
import com.gaoyun.roar.model.domain.Pet
import com.gaoyun.roar.presentation.add_pet.data.AddPetDataScreenContract

@Composable
fun PetDataFormHeader(
    avatar: String,
    petToEdit: Pet?,
    petName: MutableState<String>,
    onAvatarEditClick: (AddPetDataScreenContract.Event.NavigateToAvatarEdit) -> Unit
) {
    val activity = LocalContext.current as AppCompatActivity

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 32.dp, start = 24.dp, end = 24.dp)
    ) {
        if (petToEdit == null) {
            Image(
                painter = painterResource(id = activity.getDrawableByName(avatar)),
                contentDescription = stringResource(id = R.string.cd_pet),
                modifier = Modifier
                    .size(64.dp)
                    .padding(end = 12.dp, top = 8.dp)
            )
        } else {
            Surface(
                shape = RoundedCornerShape(8.dp),
                tonalElevation = 120.dp,
                modifier = Modifier
                    .padding(end = 12.dp, top = 8.dp)
            ) {
                Image(
                    painter = painterResource(id = activity.getDrawableByName(avatar)),
                    contentDescription = stringResource(id = R.string.cd_pet),
                    modifier = Modifier
                        .size(64.dp)
                        .clickable { onAvatarEditClick(AddPetDataScreenContract.Event.NavigateToAvatarEdit(petToEdit.id, petToEdit.petType)) }
                        .padding(all = 8.dp)
                )
            }
        }
        TextFormField(
            text = petName.value,
            leadingIcon = {
                Icon(
                    Icons.Filled.Pets,
                    stringResource(id = R.string.name),
                    tint = MaterialTheme.colorScheme.onBackground
                )
            },
            label = stringResource(id = R.string.name),
            onChange = {
                petName.value = it
            },
            imeAction = ImeAction.Done,
        )
    }
}