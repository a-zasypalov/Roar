package com.gaoyun.roar.ui.features.create_reminder

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.gaoyun.roar.model.domain.PetWithInteractions
import com.gaoyun.roar.model.domain.interactions.InteractionTemplate
import com.gaoyun.roar.presentation.add_reminder.choose_template.AddReminderScreenContract
import com.gaoyun.roar.ui.common.composables.RoarIcon
import com.gaoyun.roar.ui.common.composables.Spacer
import com.gaoyun.roar.ui.common.composables.platformStyleClickable
import com.gaoyun.roar.ui.common.ext.getDrawableByName
import com.gaoyun.roar.ui.common.ext.getName
import com.gaoyun.roar.ui.common.ext.repeatConfigTextShort
import com.gaoyun.roar.ui.common.ext.toLocalizedStringId
import com.gaoyun.roar.ui.common.icon
import com.gaoyun.roar.ui.theme.RoarTheme
import com.gaoyun.roar.ui.theme.RoarThemePreview
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import roar.sharedlib.generated.resources.Res
import roar.sharedlib.generated.resources.cd_reminder
import roar.sharedlib.generated.resources.custom
import roar.sharedlib.generated.resources.new_custom_reminder
import roar.sharedlib.generated.resources.reminder
import roar.sharedlib.generated.resources.templates

@Composable
fun TemplatesList(
    pet: PetWithInteractions,
    templates: List<InteractionTemplate>,
    templateChosen: (AddReminderScreenContract.Event.TemplateChosen) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier) {
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, top = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(getDrawableByName(pet.avatar)),
                    contentDescription = pet.name,
                    modifier = Modifier.size(48.dp)
                )

                Spacer(size = 10.dp)

                Text(
                    text = stringResource(resource = Res.string.templates),
                    style = MaterialTheme.typography.displayMedium,
                )
            }
        }

        templates.groupBy { it.group }.forEach { (interactionGroup, templates) ->
            item {
                Text(
                    text = stringResource(resource = interactionGroup.toLocalizedStringId()),
                    style = MaterialTheme.typography.headlineLarge,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)
                )
            }

            items(templates) { template ->
                TemplateItem(
                    template = template,
                    isUsed = pet.interactions.values.flatten()
                        .any { it.isActive && it.templateId == template.id },
                    onClick = { templateId ->
                        templateChosen(
                            AddReminderScreenContract.Event.TemplateChosen(
                                templateId = templateId,
                                petId = pet.id
                            )
                        )
                    }
                )
            }
        }

        item {
            Text(
                text = stringResource(resource = Res.string.custom),
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)
            )
        }

        item {
            Surface(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                shape = RoundedCornerShape(16.dp),
                tonalElevation = RoarTheme.CONTENT_CARD_ELEVATION,
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .platformStyleClickable {
                            templateChosen(
                                AddReminderScreenContract.Event.TemplateChosen(
                                    templateId = "null",
                                    petId = pet.id
                                )
                            )
                        }
                        .padding(horizontal = 14.dp, vertical = 8.dp),
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RoarIcon(
                            icon = pet.petType.icon(),
                            contentDescription = stringResource(resource = Res.string.reminder),
                            modifier = Modifier
                                .padding(4.dp)
                                .size(32.dp)
                        )
                        Spacer(size = 8.dp)
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                stringResource(resource = Res.string.custom),
                                style = MaterialTheme.typography.titleLarge,
                                color = MaterialTheme.colorScheme.onSurface
                            )

                            Text(
                                stringResource(resource = Res.string.new_custom_reminder),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }
        }

        item {
            Spacer(size = 56.dp)
        }
    }
}

@Composable
fun TemplateItem(
    template: InteractionTemplate,
    isUsed: Boolean,
    onClick: (String) -> Unit
) {
    Surface(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        tonalElevation = RoarTheme.CONTENT_CARD_ELEVATION,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .platformStyleClickable { onClick(template.id) }
                .padding(horizontal = 14.dp, vertical = 8.dp),
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                RoarIcon(
                    icon = template.type.icon(),
                    contentDescription = stringResource(resource = Res.string.cd_reminder),
                    modifier = Modifier
                        .padding(4.dp)
                        .size(32.dp)
                )
                Spacer(size = 8.dp)
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = template.getName(),
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Text(
                        template.repeatConfig.repeatConfigTextShort(),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                if (isUsed) {
                    Icon(Icons.Default.Check, contentDescription = null)
                }
            }
        }
    }
}

@Composable
@Preview
fun TemplatesListPreview() {
    RoarThemePreview {
        TemplatesList(pet = PetWithInteractions.preview(), templates = listOf(
            InteractionTemplate.preview,
            InteractionTemplate.preview,
            InteractionTemplate.preview,
        ), templateChosen = {})
    }
}