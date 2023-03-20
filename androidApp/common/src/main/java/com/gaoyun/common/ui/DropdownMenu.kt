package com.gaoyun.common.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownMenu(
    valueList: List<String>,
    listState: MutableState<String>,
    modifier: Modifier = Modifier,
    onChange: ((String) -> Unit)? = null,
    @StringRes valueDisplayList: List<Int>?,
    @StringRes listDisplayState: Int?,
    label: String? = null,
    leadingIcon: ImageVector? = null
) {
    var menuExpanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = menuExpanded,
        onExpandedChange = { menuExpanded = !menuExpanded },
        modifier = modifier
    ) {
        TextFormField(
            readOnly = true,
            text = listDisplayState?.let { stringResource(id = it) } ?: listState.value,
            onChange = { },
            label = label,
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = menuExpanded)
            },
            leadingIcon = leadingIcon?.let {
                {
                    Icon(
                        it,
                        "",
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor()
        )
        ExposedDropdownMenu(
            expanded = menuExpanded,
            onDismissRequest = { menuExpanded = false },
            modifier = Modifier.background(MaterialTheme.colorScheme.surfaceVariant)
        ) {
            valueList.forEachIndexed { index, selection ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = valueDisplayList?.let { stringResource(id = it[index]) } ?: selection,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    },
                    onClick = {
                        onChange?.invoke(selection)
                        listState.value = selection
                        menuExpanded = false
                    },
                )
            }
        }
    }
}