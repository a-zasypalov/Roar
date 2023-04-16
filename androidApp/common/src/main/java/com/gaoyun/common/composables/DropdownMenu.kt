@file:OptIn(ExperimentalMaterial3Api::class)

package com.gaoyun.common.composables

import androidx.annotation.PluralsRes
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource

@Composable
fun DropdownMenu(
    valueList: List<String>,
    listState: MutableState<String>,
    modifier: Modifier = Modifier,
    onChange: ((String) -> Unit)? = null,
    label: String? = null,
    leadingIcon: ImageVector? = null
) {
    DropdownMenuInternal(
        valueList = valueList,
        listState = listState,
        modifier = modifier,
        onChange = onChange,
        valueDisplayList = null,
        listDisplayState = null,
        label = label,
        leadingIcon = leadingIcon,
    )
}

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
    DropdownMenuInternal(
        valueList = valueList,
        listState = listState,
        modifier = modifier,
        onChange = onChange,
        valueDisplayList = valueDisplayList?.map { stringResource(id = it) },
        listDisplayState = listDisplayState?.let { stringResource(id = it) },
        label = label,
        leadingIcon = leadingIcon,
    )
}

@Composable
fun DropdownMenu(
    valueList: List<String>,
    listState: MutableState<String>,
    modifier: Modifier = Modifier,
    onChange: ((String) -> Unit)? = null,
    @StringRes valueDisplayList: List<Int>?,
    @PluralsRes listDisplayState: Int?,
    listDisplayStateQuantity: Int,
    label: String? = null,
    leadingIcon: ImageVector? = null
) {
    DropdownMenuInternal(
        valueList = valueList,
        listState = listState,
        modifier = modifier,
        onChange = onChange,
        valueDisplayList = valueDisplayList?.map { stringResource(id = it) },
        listDisplayState = listDisplayState?.let { pluralStringResource(id = it, count = listDisplayStateQuantity) },
        label = label,
        leadingIcon = leadingIcon,
    )
}

@Composable
private fun DropdownMenuInternal(
    valueList: List<String>,
    listState: MutableState<String>,
    modifier: Modifier = Modifier,
    onChange: ((String) -> Unit)? = null,
    valueDisplayList: List<String>?,
    listDisplayState: String?,
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
            text = listDisplayState ?: listState.value,
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
                            text = valueDisplayList?.let { it[index] } ?: selection,
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