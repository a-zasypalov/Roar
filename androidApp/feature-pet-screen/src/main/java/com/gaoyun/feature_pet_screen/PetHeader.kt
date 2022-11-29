package com.gaoyun.feature_pet_screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.gaoyun.common.DateUtils.monthsFromNow
import com.gaoyun.common.DateUtils.yearsFromNow
import com.gaoyun.common.ui.Spacer
import com.gaoyun.common.ui.getDrawableByName
import com.gaoyun.roar.model.domain.Gender
import com.gaoyun.roar.model.domain.Pet

@Composable
internal fun PetHeader(
    pet: Pet,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val isSterilized = remember { if (pet.isSterilized) "sterilized" else "not sterilized" }

    Column(
        modifier = modifier.padding(horizontal = 8.dp),
        horizontalAlignment = Alignment.Start
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = pet.name,
                style = MaterialTheme.typography.displayMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(0.8f)
            )

            Spacer(8.dp)

            Surface(
                tonalElevation = 8.dp,
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(0.2f),
                shape = CircleShape,
            ) {
                Image(
                    painter = painterResource(id = context.getDrawableByName(pet.avatar)),
                    contentDescription = pet.name,
                    modifier = Modifier
                        .height(72.dp)
                        .padding(all = 12.dp)
                )
            }
        }

        Spacer(size = 32.dp)

        Card(
            elevation = CardDefaults.elevatedCardElevation(0.dp),
            shape = MaterialTheme.shapes.large,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
            ) {
                TextWithIconBulletPoint(icon = Icons.Filled.Cake, "${pet.birthday.yearsFromNow()} years ${pet.birthday.monthsFromNow()} months")

                when (pet.gender) {
                    Gender.MALE -> TextWithIconBulletPoint(icon = Icons.Filled.Male, "Male, $isSterilized")
                    Gender.FEMALE -> TextWithIconBulletPoint(icon = Icons.Filled.Female, "Female, $isSterilized")
                }

                TextWithIconBulletPoint(icon = Icons.Filled.Pets, pet.breed)

                if (pet.chipNumber.isNotEmpty()) {
                    TextWithIconBulletPoint(icon = Icons.Filled.Memory, "Chip: ${pet.chipNumber}")
                }
            }
        }
    }
}

@Composable
fun TextWithIconBulletPoint(icon: ImageVector, text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 4.dp)
    ) {
        Icon(
            icon,
            text,
            tint = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(4.dp),
        )
        Spacer(size = 12.dp)
        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}