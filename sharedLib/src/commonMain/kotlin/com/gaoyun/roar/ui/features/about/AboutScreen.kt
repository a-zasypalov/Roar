package com.gaoyun.roar.ui.features.about

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
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.gaoyun.roar.presentation.BackNavigationEffect
import com.gaoyun.roar.presentation.LAUNCH_LISTEN_FOR_EFFECTS
import com.gaoyun.roar.presentation.NavigationSideEffect
import com.gaoyun.roar.presentation.about_screen.AboutScreenContract
import com.gaoyun.roar.presentation.about_screen.AboutScreenViewModel
import com.gaoyun.roar.ui.common.composables.Spacer
import com.gaoyun.roar.ui.common.composables.SurfaceScaffold
import com.gaoyun.roar.ui.theme.RoarTheme
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import moe.tlaster.precompose.koin.koinViewModel
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import roar.sharedlib.generated.resources.Res
import roar.sharedlib.generated.resources.about_app_description
import roar.sharedlib.generated.resources.about_app_title
import roar.sharedlib.generated.resources.app_name
import roar.sharedlib.generated.resources.ic_tab_home
import roar.sharedlib.generated.resources.ic_telegram
import roar.sharedlib.generated.resources.ic_twitter
import roar.sharedlib.generated.resources.privacy_policy
import roar.sharedlib.generated.resources.terms_and_conditions_button
import roar.sharedlib.generated.resources.url_email
import roar.sharedlib.generated.resources.url_privacy_policy
import roar.sharedlib.generated.resources.url_telegram
import roar.sharedlib.generated.resources.url_terms_and_conditions
import roar.sharedlib.generated.resources.url_twitter
import roar.sharedlib.generated.resources.url_web

@Composable
fun AboutScreenDestination(
    onNavigationCall: (NavigationSideEffect) -> Unit,
) {
    val viewModel = koinViewModel(vmClass = AboutScreenViewModel::class)

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

@OptIn(ExperimentalResourceApi::class)
@Composable
fun AboutScreenContent() {
    val scrollState = rememberScrollState()
    val uriHandler = LocalUriHandler.current

    val termsAndConditionsUrl = stringResource(resource = Res.string.url_terms_and_conditions)
    val privacyPolicyUrl = stringResource(resource = Res.string.url_privacy_policy)

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
            text = stringResource(Res.string.about_app_title),
            style = MaterialTheme.typography.displayMedium,
            color = MaterialTheme.colorScheme.onSurface,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(size = 32.dp)

        Surface(
            tonalElevation = RoarTheme.IMAGE_ITEM_ELEVATION,
            modifier = Modifier
                .clip(MaterialTheme.shapes.extraLarge)
                .size(120.dp)
        ) {
            Image(
                painter = painterResource(resource = Res.drawable.ic_tab_home),
                contentDescription = "icon",
                modifier = Modifier.padding(16.dp),
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.inverseSurface)
            )
        }

        Spacer(size = 32.dp)

        Text(
            text = stringResource(resource = Res.string.about_app_description),
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(size = 32.dp)

        LinksBlock()

        Spacer(size = 32.dp)

        TextButton(
            onClick = { uriHandler.openUri(termsAndConditionsUrl) },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(
                text = stringResource(resource = Res.string.terms_and_conditions_button),
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 32.dp)
            )
        }

        TextButton(
            onClick = { uriHandler.openUri(privacyPolicyUrl) },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(
                text = stringResource(resource = Res.string.privacy_policy),
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 32.dp),
            )
        }

        Spacer(size = 64.dp)

    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
private fun LinksBlock() {
    val uriHandler = LocalUriHandler.current

    val telegramUrl = stringResource(resource = Res.string.url_telegram)
    val twitterUrl = stringResource(resource = Res.string.url_twitter)
    val webUrl = stringResource(resource = Res.string.url_web)

    val email = stringResource(resource = Res.string.url_email)
    val subject = stringResource(resource = Res.string.app_name)

    Row {
        LinkItem(
            painterResource(resource = Res.drawable.ic_telegram),
            Modifier.clickable { uriHandler.openUri(telegramUrl) }
        )
        Spacer(size = 24.dp)
        LinkItem(
            Icons.Default.Mail,
            Modifier.clickable { /*context.sendMail(to = email, subject = subject)*/ }
        )
        Spacer(size = 24.dp)
        LinkItem(
            painterResource(resource = Res.drawable.ic_twitter),
            Modifier.clickable { uriHandler.openUri(twitterUrl) }
        )
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
        tonalElevation = RoarTheme.CLICKABLE_ITEM_ELEVATION,
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

//fun Context.sendMail(to: String, subject: String) {
//    try {
//        val intent = Intent(Intent.ACTION_SEND)
//        intent.type = "message/rfc822"
//        intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(to))
//        intent.putExtra(Intent.EXTRA_SUBJECT, subject)
//        startActivity(intent)
//    } catch (e: ActivityNotFoundException) {
//        // TODO: Handle case where no email app is available
//    } catch (t: Throwable) {
//        // TODO: Handle potential other type of exceptions
//    }
//}
//
//@Composable
//@Preview(device = Devices.PIXEL)
//fun AboutScreenPreview() {
//    RoarThemePreview {
//        SurfaceScaffold {
//            AboutScreenContent()
//        }
//    }
//}