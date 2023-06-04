package com.gaoyun.feature_user_screen.about_screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gaoyun.common.R
import com.gaoyun.common.composables.Spacer
import com.gaoyun.common.composables.SurfaceScaffold
import com.gaoyun.common.theme.RoarTheme
import com.gaoyun.roar.presentation.BackNavigationEffect
import com.gaoyun.roar.presentation.LAUNCH_LISTEN_FOR_EFFECTS
import com.gaoyun.roar.presentation.NavigationSideEffect
import com.gaoyun.roar.presentation.about_screen.AboutScreenContract
import com.gaoyun.roar.presentation.about_screen.AboutScreenViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.compose.getViewModel

@Composable
fun AboutScreenDestination(
    onNavigationCall: (NavigationSideEffect) -> Unit,
) {
    val viewModel: AboutScreenViewModel = getViewModel()

    LaunchedEffect(LAUNCH_LISTEN_FOR_EFFECTS) {
        viewModel.effect.onEach { effect ->
            when (effect) {
                is AboutScreenContract.Effect.NavigateBack -> onNavigationCall(BackNavigationEffect)
            }
        }.collect()
    }

    SurfaceScaffold(
        backHandler = { viewModel.setEvent(AboutScreenContract.Event.NavigateBack) },
    ) {
        AboutScreenContent()
    }
}

@Composable
fun AboutScreenContent() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = 16.dp,
                end = 16.dp,
                top = 8.dp
            )
    ) {
        Text(
            text = stringResource(R.string.about_app_title),
            style = MaterialTheme.typography.displayMedium,
            color = MaterialTheme.colorScheme.onSurface,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(size = 32.dp)

        Surface(
            tonalElevation = 16.dp,
            modifier = Modifier
                .clip(MaterialTheme.shapes.extraLarge)
                .size(120.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_tab_home),
                contentDescription = "icon",
                modifier = Modifier.padding(16.dp),
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.inverseSurface)
            )
        }

        Spacer(size = 32.dp)

        Text(text = stringResource(id = R.string.about_app_description), style = MaterialTheme.typography.titleMedium)

        Spacer(size = 32.dp)

        Row {
            Surface(
                tonalElevation = 16.dp,
                modifier = Modifier
                    .clip(MaterialTheme.shapes.medium)
                    .clickable { }
            ) {
                Icon(
                    painterResource(
                        id = R.drawable.ic_telegram
                    ),
                    contentDescription = null,
                    modifier = Modifier
                        .size(48.dp)
                        .padding(8.dp)
                )
            }

            Spacer(size = 24.dp)

            Surface(
                tonalElevation = 16.dp,
                modifier = Modifier
                    .clip(MaterialTheme.shapes.medium)
                    .clickable { }
            ) {
                Icon(
                    Icons.Default.Mail,
                    contentDescription = null,
                    modifier = Modifier
                        .size(48.dp)
                        .padding(8.dp)
                )
            }

            Spacer(size = 24.dp)

            Surface(
                tonalElevation = 16.dp,
                modifier = Modifier
                    .clip(MaterialTheme.shapes.medium)
                    .clickable { }
            ) {
                Icon(
                    painterResource(id = R.drawable.ic_twitter),
                    contentDescription = null,
                    modifier = Modifier
                        .size(48.dp)
                        .padding(8.dp)
                )
            }
        }
    }
}

@Composable
@Preview(device = Devices.PIXEL)
fun AboutScreenPreview() {
    RoarTheme {
        SurfaceScaffold {
            AboutScreenContent()
        }
    }
}