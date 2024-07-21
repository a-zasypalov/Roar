package com.gaoyun.roar.ui.features.home.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.gaoyun.roar.ui.common.composables.Spacer
import com.gaoyun.roar.ui.common.composables.SurfaceCard
import com.gaoyun.roar.ui.common.composables.platformStyleClickable
import com.gaoyun.roar.ui.theme.primaryColor
import com.gaoyun.roar.util.ColorTheme
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import roar.sharedlib.generated.resources.Res
import roar.sharedlib.generated.resources.customization_prompt_caption
import roar.sharedlib.generated.resources.customization_prompt_title
import roar.sharedlib.generated.resources.ic_launcher_paw_foreground

@Composable
internal fun CustomizationPrompt(onCloseClick: () -> Unit, modifier: Modifier = Modifier) {
    SurfaceCard(
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = modifier
    ) {
        Box {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "close",
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(2.dp)
                    .platformStyleClickable(onClick = onCloseClick)
            )
            Row(modifier = Modifier.fillMaxWidth().padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
                Column {
                    Text(text = stringResource(Res.string.customization_prompt_title), style = MaterialTheme.typography.labelLarge)
                    Text(text = stringResource(Res.string.customization_prompt_caption), style = MaterialTheme.typography.labelSmall)
                }
                Spacer(size = 16.dp)
                Surface(
                    shape = MaterialTheme.shapes.small,
                    color = Color.White,
                    modifier = Modifier.size(32.dp)
                ) {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        userScrollEnabled = false,
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(1.dp),
                        horizontalArrangement = Arrangement.spacedBy(1.dp)
                    ) {
                        items(ColorTheme.entries.take(4), key = { it.ordinal }) { theme ->
                            Box(
                                modifier = Modifier
                                    .background(theme.primaryColor(isSystemInDarkTheme()))
                                    .size(16.dp)
                            )
                        }
                    }
                }
                Spacer(size = 8.dp)
                Surface(
                    shape = MaterialTheme.shapes.small,
                    color = Color.White,
                    modifier = Modifier.size(32.dp)
                ) {
                    Image(
                        painterResource(Res.drawable.ic_launcher_paw_foreground),
                        contentDescription = null,
                        contentScale = ContentScale.FillWidth,
                    )
                }
                androidx.compose.foundation.layout.Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}
