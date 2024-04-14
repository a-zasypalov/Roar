package com.gaoyun.feature_home_screen.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.gaoyun.common.R
import com.gaoyun.roar.ui.Spacer
import com.gaoyun.common.ext.getDrawableByName
import com.gaoyun.roar.model.domain.PetWithInteractions

@Composable
fun InteractionPetChooser(pets: List<PetWithInteractions>, onPetChosen: (String) -> Unit, onDismiss: () -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            elevation = CardDefaults.elevatedCardElevation(0.dp),
            shape = MaterialTheme.shapes.large,
        ) {
            LazyColumn {
                item {
                    Spacer(8.dp)

                    Text(
                        text = stringResource(id = R.string.for_who),
                        style = MaterialTheme.typography.displaySmall,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                        textAlign = TextAlign.Center
                    )
                }
                items(pets) { pet ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onPetChosen(pet.id) }
                            .padding(horizontal = 16.dp, vertical = 12.dp)
                    ) {

                        Image(
                            painter = painterResource(id = LocalContext.current.getDrawableByName(pet.avatar)),
                            contentDescription = pet.name,
                            modifier = Modifier.height(56.dp)
                        )

                        Spacer(16.dp)

                        Text(
                            text = pet.name,
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.weight(0.8f)
                        )
                    }
                }
            }
        }
    }
}