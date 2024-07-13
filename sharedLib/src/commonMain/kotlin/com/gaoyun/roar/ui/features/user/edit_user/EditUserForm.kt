package com.gaoyun.roar.ui.features.user.edit_user

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.gaoyun.roar.model.domain.User
import com.gaoyun.roar.presentation.user_edit.EditUserScreenContract
import com.gaoyun.roar.ui.common.composables.PrimaryElevatedButtonOnSurface
import com.gaoyun.roar.ui.common.composables.Spacer
import com.gaoyun.roar.ui.common.composables.SurfaceCard
import com.gaoyun.roar.ui.common.composables.TextFormField
import com.gaoyun.roar.ui.common.composables.surfaceCardFormElevation
import com.gaoyun.roar.ui.common.composables.surfaceCardFormShape
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import roar.sharedlib.generated.resources.Res
import roar.sharedlib.generated.resources.name
import roar.sharedlib.generated.resources.save

@Composable
internal fun EditUserForm(
    user: User, onSaveClick: (EditUserScreenContract.Event.OnSaveAccountClick) -> Unit
) {
    val userName = remember { mutableStateOf(user.name) }

    SurfaceCard(
        shape = surfaceCardFormShape, elevation = surfaceCardFormElevation(),
        modifier = Modifier.padding(horizontal = 6.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp)
                .navigationBarsPadding()
        ) {
            Spacer(size = 32.dp)

            TextFormField(
                text = userName.value,
                leadingIcon = {
                    Icon(
                        Icons.Filled.Person,
                        contentDescription = stringResource(resource = Res.string.name),
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                },
                label = stringResource(resource = Res.string.name),
                onChange = {
                    userName.value = it
                },
                imeAction = ImeAction.Done,
            )

            Spacer(size = 32.dp)

            PrimaryElevatedButtonOnSurface(
                text = stringResource(resource = Res.string.save),
                onClick = { onSaveClick(EditUserScreenContract.Event.OnSaveAccountClick(user.copy(name = userName.value))) },
            )

            Spacer(size = 32.dp)
        }
    }
}

@Preview
@Composable
fun EditUserScreenPreview() {
    EditUserForm(User("id", "Tester")) {}
}