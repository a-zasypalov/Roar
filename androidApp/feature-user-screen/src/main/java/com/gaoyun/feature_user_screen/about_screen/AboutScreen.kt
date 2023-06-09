package com.gaoyun.feature_user_screen.about_screen

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Language
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
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
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
    val scrollState = rememberScrollState()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(scrollState)
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

        LinksBlock()
    }
}

@Composable
private fun LinksBlock() {
    val context = LocalContext.current
    val uriHandler = LocalUriHandler.current

    val telegramUrl = stringResource(id = R.string.url_telegram)
    val twitterUrl = stringResource(id = R.string.url_twitter)
    val webUrl = stringResource(id = R.string.url_web)

    val email = stringResource(id = R.string.url_email)
    val subject = stringResource(id = R.string.app_name)

    Row {
        LinkItem(painterResource(id = R.drawable.ic_telegram), Modifier.clickable { uriHandler.openUri(telegramUrl) })
        Spacer(size = 24.dp)
        LinkItem(Icons.Default.Mail, Modifier.clickable { context.sendMail(to = email, subject = subject) })
        Spacer(size = 24.dp)
        LinkItem(painterResource(id = R.drawable.ic_twitter), Modifier.clickable { uriHandler.openUri(twitterUrl) })
        Spacer(size = 24.dp)
        LinkItem(Icons.Default.Language, Modifier.clickable { uriHandler.openUri(webUrl) })
    }
}

@Composable
private fun LinkItem(icon: ImageVector, modifier: Modifier = Modifier) {
    LinkItem(
        icon = rememberVectorPainter(image = icon),
        modifier = modifier
    )
}

@Composable
private fun LinkItem(icon: Painter, modifier: Modifier = Modifier) {
    Surface(
        tonalElevation = 16.dp,
        modifier = modifier.clip(MaterialTheme.shapes.medium)
    ) {
        Icon(
            icon,
            contentDescription = null,
            modifier = Modifier
                .size(48.dp)
                .padding(8.dp)
        )
    }
}

fun Context.sendMail(to: String, subject: String) {
    try {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "message/rfc822"
        intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(to))
        intent.putExtra(Intent.EXTRA_SUBJECT, subject)
        startActivity(intent)
    } catch (e: ActivityNotFoundException) {
        // TODO: Handle case where no email app is available
    } catch (t: Throwable) {
        // TODO: Handle potential other type of exceptions
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