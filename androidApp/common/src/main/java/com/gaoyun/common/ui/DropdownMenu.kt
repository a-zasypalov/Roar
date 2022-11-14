package com.gaoyun.common.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownMenu(
    valueList: List<String>,
    listState: MutableState<String>,
    modifier: Modifier = Modifier,
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
            text = listState.value,
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
            valueList.forEach { breedSelection ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = breedSelection,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    },
                    onClick = {
                        listState.value = breedSelection
                        menuExpanded = false
                    },
                )
            }
        }
    }
}