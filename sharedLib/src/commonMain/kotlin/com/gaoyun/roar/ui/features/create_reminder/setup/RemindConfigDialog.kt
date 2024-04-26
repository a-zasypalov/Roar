package com.gaoyun.roar.ui.features.create_reminder.setup

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.gaoyun.roar.model.domain.interactions.InteractionRemindConfig
import com.gaoyun.roar.model.domain.interactions.InteractionRemindConfigPeriod
import com.gaoyun.roar.model.domain.interactions.toInteractionRemindConfigPeriod
import com.gaoyun.roar.ui.common.composables.DropdownMenu
import com.gaoyun.roar.ui.common.composables.Spacer
import com.gaoyun.roar.ui.common.composables.SurfaceCard
import com.gaoyun.roar.ui.common.composables.TextFormField
import com.gaoyun.roar.ui.common.ext.toLocalizedStringId
import com.gaoyun.roar.ui.common.ext.toLocalizedStringIdPlural
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource
import roar.sharedlib.generated.resources.Res
import roar.sharedlib.generated.resources.cancel
import roar.sharedlib.generated.resources.day
import roar.sharedlib.generated.resources.done
import roar.sharedlib.generated.resources.remind

@OptIn(ExperimentalResourceApi::class)
@Composable
internal fun RemindConfigDialog(
    remindConfig: InteractionRemindConfig?,
    setShowDialog: (Boolean) -> Unit,
    onConfigSave: (String) -> Unit
) {
    val dayString = stringResource(Res.string.day)
    val repeatsEveryNumber = rememberSaveable { mutableStateOf(remindConfig?.remindBeforeNumber?.toString() ?: "1") }
    val repeatsEveryPeriod = rememberSaveable { mutableStateOf(remindConfig?.repeatsEveryPeriod?.toString() ?: dayString) }
    val repeatsEveryPeriodsList = InteractionRemindConfigPeriod.LIST

    val defaultHorizontalPadding = 24.dp

    Dialog(
        onDismissRequest = { setShowDialog(false) },
        properties = DialogProperties(
            dismissOnClickOutside = false
        )
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            SurfaceCard(
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .padding(vertical = 16.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Text(
                        text = stringResource(resource = Res.string.remind),
                        style = MaterialTheme.typography.displaySmall,
                        modifier = Modifier.padding(horizontal = defaultHorizontalPadding)
                    )

                    Spacer(size = 8.dp)

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = defaultHorizontalPadding)
                    ) {
                        TextFormField(
                            text = repeatsEveryNumber.value,
                            keyboardType = KeyboardType.Decimal,
                            onChange = { repeatsEveryNumber.value = it },
                            modifier = Modifier.fillMaxWidth(0.25f),
                            imeAction = ImeAction.Done,
                        )
                        Spacer(size = 12.dp)
                        DropdownMenu(
                            valueList = repeatsEveryPeriodsList,
                            listState = repeatsEveryPeriod,
                            valueDisplayList = repeatsEveryPeriodsList.map { it.toInteractionRemindConfigPeriod().toLocalizedStringId() },
                            listDisplayState = repeatsEveryPeriod.value.toInteractionRemindConfigPeriod().toLocalizedStringIdPlural(),
                            listDisplayStateQuantity = repeatsEveryNumber.value.toIntOrNull() ?: 1,
                            modifier = Modifier.fillMaxWidth(1f)
                        )
                    }

                    Spacer(12.dp)

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = defaultHorizontalPadding),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(onClick = {
                            setShowDialog(false)
                        }) {
                            Text(
                                text = stringResource(resource = Res.string.cancel)
                            )
                        }

                        Spacer(size = 12.dp)

                        TextButton(onClick = {
                            val config = "${repeatsEveryNumber.value}_${repeatsEveryPeriod.value}"

                            onConfigSave(config)
                            setShowDialog(false)
                        }) {
                            Text(
                                text = stringResource(resource = Res.string.done)
                            )
                        }
                    }
                }
            }
        }
    }
}