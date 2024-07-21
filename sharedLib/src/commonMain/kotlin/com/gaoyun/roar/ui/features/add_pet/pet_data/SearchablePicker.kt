package com.gaoyun.roar.ui.features.add_pet.pet_data

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.gaoyun.roar.ui.common.composables.Spacer
import com.gaoyun.roar.ui.common.composables.TextFormField
import com.gaoyun.roar.ui.common.composables.platformStyleClickable
import org.jetbrains.compose.resources.stringResource
import roar.sharedlib.generated.resources.Res
import roar.sharedlib.generated.resources.search

@Composable
fun SearchablePicker(
    items: List<String>,
    onItemSelected: (String) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    val filteredItems = items.filter { it.contains(searchQuery, ignoreCase = true) }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        TextFormField(
            text = searchQuery,
            leadingIcon = {
                Icon(
                    Icons.Filled.Search,
                    stringResource(resource = Res.string.search),
                    tint = MaterialTheme.colorScheme.onBackground
                )
            },
            label = stringResource(resource = Res.string.search),
            onChange = { searchQuery = it },
            imeAction = ImeAction.Done,
        )

        Spacer(size = 8.dp)

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(filteredItems.size) { index ->
                val item = filteredItems[index]
                Text(
                    text = item,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .platformStyleClickable { onItemSelected(item) }
                )
            }
        }
    }
}