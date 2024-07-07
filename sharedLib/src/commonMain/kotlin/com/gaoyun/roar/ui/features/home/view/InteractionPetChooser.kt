package com.gaoyun.roar.ui.features.home.view

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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.gaoyun.roar.model.domain.PetWithInteractions
import com.gaoyun.roar.ui.common.composables.Spacer
import com.gaoyun.roar.ui.common.ext.getDrawableByName
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import roar.sharedlib.generated.resources.Res
import roar.sharedlib.generated.resources.for_who

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
                        text = stringResource(resource = Res.string.for_who),
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
                            painter = painterResource(getDrawableByName(pet.avatar)),
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