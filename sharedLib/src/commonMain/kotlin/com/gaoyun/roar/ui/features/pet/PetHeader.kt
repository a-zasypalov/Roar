package com.gaoyun.roar.ui.features.pet

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cake
import androidx.compose.material.icons.filled.Female
import androidx.compose.material.icons.filled.Male
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gaoyun.roar.model.domain.Gender
import com.gaoyun.roar.model.domain.Pet
import com.gaoyun.roar.ui.common.ageText
import com.gaoyun.roar.ui.common.composables.AutoResizeText
import com.gaoyun.roar.ui.common.composables.FontSizeRange
import com.gaoyun.roar.ui.common.composables.Spacer
import com.gaoyun.roar.ui.common.ext.getDrawableByName
import com.gaoyun.roar.ui.theme.RoarTheme
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import roar.sharedlib.generated.resources.Res
import roar.sharedlib.generated.resources.chip
import roar.sharedlib.generated.resources.female
import roar.sharedlib.generated.resources.male
import roar.sharedlib.generated.resources.not_sterilized
import roar.sharedlib.generated.resources.sterilized

@Composable
internal fun PetHeader(
    pet: Pet,
    modifier: Modifier = Modifier
) {
    val isSterilized = if (pet.isSterilized) {
        stringResource(resource = Res.string.sterilized)
    } else {
        stringResource(resource = Res.string.not_sterilized)
    }


    Column(
        modifier = modifier.padding(horizontal = 8.dp),
        horizontalAlignment = Alignment.Start
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            AutoResizeText(
                text = pet.name,
                maxLines = 2,
                style = MaterialTheme.typography.displayMedium,
                color = MaterialTheme.colorScheme.onSurface,
                fontSizeRange = FontSizeRange(
                    min = 20.sp,
                    max = MaterialTheme.typography.displayMedium.fontSize,
                ),
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(0.8f)
            )

            Spacer(8.dp)

            Surface(
                tonalElevation = RoarTheme.INFORMATION_CARD_ELEVATION,
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(0.2f),
                shape = CircleShape,
            ) {
                Image(
                    painter = painterResource(getDrawableByName(pet.avatar)),
                    contentDescription = pet.name,
                    modifier = Modifier
                        .height(72.dp)
                        .padding(all = 12.dp)
                )
            }
        }

        Spacer(size = 16.dp)

        Surface(
            tonalElevation = RoarTheme.INFORMATION_CARD_ELEVATION,
            shape = MaterialTheme.shapes.large,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
            ) {
                TextWithIconBulletPoint(
                    icon = Icons.Filled.Cake,
                    text = pet.ageText()
                )

                when (pet.gender) {
                    Gender.MALE -> TextWithIconBulletPoint(
                        icon = Icons.Filled.Male,
                        "${stringResource(resource = Res.string.male)}, $isSterilized"
                    )

                    Gender.FEMALE -> TextWithIconBulletPoint(
                        icon = Icons.Filled.Female,
                        "${stringResource(resource = Res.string.female)}, $isSterilized"
                    )
                }

                TextWithIconBulletPoint(icon = Icons.Filled.Pets, pet.breed)

                if (pet.chipNumber.isNotEmpty()) {
                    TextWithIconBulletPoint(
                        icon = Icons.Filled.Memory,
                        "${stringResource(resource = Res.string.chip)}: ${pet.chipNumber}"
                    )
                }
            }
        }
    }
}

@Composable
private fun TextWithIconBulletPoint(icon: ImageVector, text: String) {
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