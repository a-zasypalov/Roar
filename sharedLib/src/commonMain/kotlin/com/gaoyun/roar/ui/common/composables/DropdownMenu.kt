@file:OptIn(ExperimentalMaterial3Api::class)

package com.gaoyun.roar.ui.common.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

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

@OptIn(ExperimentalResourceApi::class)
@Composable
fun DropdownMenu(
    valueList: List<String>,
    listState: MutableState<String>,
    modifier: Modifier = Modifier,
    onChange: ((String) -> Unit)? = null,
    valueDisplayList: List<StringResource>?,
    listDisplayState: StringResource?,
    label: String? = null,
    leadingIcon: ImageVector? = null
) {
    DropdownMenuInternal(
        valueList = valueList,
        listState = listState,
        modifier = modifier,
        onChange = onChange,
        valueDisplayList = valueDisplayList?.map { stringResource(it) },
        listDisplayState = listDisplayState?.let { stringResource(it) },
        label = label,
        leadingIcon = leadingIcon,
    )
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun DropdownMenu(
    valueList: List<String>,
    listState: MutableState<String>,
    modifier: Modifier = Modifier,
    onChange: ((String) -> Unit)? = null,
    valueDisplayList: List<StringResource>?,
    listDisplayState: StringResource?,
    listDisplayStateQuantity: Int,
    label: String? = null,
    leadingIcon: ImageVector? = null
) {
    DropdownMenuInternal(
        valueList = valueList,
        listState = listState,
        modifier = modifier,
        onChange = onChange,
        valueDisplayList = valueDisplayList?.map { stringResource(it) },
        listDisplayState = listDisplayState?.let { stringResource(it, listDisplayStateQuantity) },
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