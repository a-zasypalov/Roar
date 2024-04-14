package com.gaoyun.roar.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.gaoyun.roar.ui.theme.colors.BlueColor
import com.gaoyun.roar.ui.theme.colors.GreenColor
import com.gaoyun.roar.ui.theme.colors.OrangeColor
import com.gaoyun.roar.util.ColorTheme

private val RoarOrange = Color(0xFFee9338)

@Composable
fun RoarTheme(
    colors: ColorScheme,
    content: @Composable () -> Unit
) {
    val typography = MaterialTheme.typography.copy(
        displayMedium = MaterialTheme.typography.displayMedium.copy(fontWeight = FontWeight.SemiBold),
        displaySmall = MaterialTheme.typography.displaySmall.copy(fontWeight = FontWeight.Medium),
        headlineLarge = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Medium),
        headlineMedium = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.SemiBold),
        headlineSmall = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Medium),
    )

    MaterialTheme(
        typography = typography,
        colorScheme = colors,
        content = content
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoarThemePreview(
    content: @Composable () -> Unit
) {
    RoarTheme(BlueColor.LightColors) {
        Scaffold {
            content()
        }
    }
}

fun ColorTheme.primaryColor(darkTheme: Boolean) = when (this) {
    ColorTheme.Green -> if (darkTheme) GreenColor.DarkColors.primary else GreenColor.LightColors.primary
    ColorTheme.Blue -> if (darkTheme) BlueColor.DarkColors.primary else BlueColor.LightColors.primary
    ColorTheme.Orange -> if (darkTheme) OrangeColor.DarkColors.primary else OrangeColor.LightColors.primary
}

object RoarTheme {

    val BACKGROUND_SURFACE_ELEVATION = 1.dp

    val IMAGE_ITEM_ELEVATION = 16.dp
    val CLICKABLE_ITEM_ELEVATION = 16.dp
    val PET_CARD_ELEVATION = 16.dp
    val CONTENT_CARD_ELEVATION = 12.dp
    val INTERACTION_CARD_ELEVATION = 24.dp
    val INACTIVE_INTERACTION_CARD_ELEVATION = BACKGROUND_SURFACE_ELEVATION + 6.dp
    val INFORMATION_CARD_ELEVATION = 64.dp

}