package com.gaoyun.roar.ui.features.onboarding

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.dp
import com.gaoyun.roar.presentation.onboarding.OnboardingViewModel
import com.gaoyun.roar.ui.common.composables.PrimaryElevatedButton
import com.gaoyun.roar.ui.common.composables.Spacer
import com.gaoyun.roar.ui.common.composables.SurfaceScaffold
import com.gaoyun.roar.ui.navigation.NavigationKeys
import com.gaoyun.roar.ui.theme.RoarTheme
import kotlinx.coroutines.launch
import moe.tlaster.precompose.koin.koinViewModel
import moe.tlaster.precompose.navigation.NavOptions
import moe.tlaster.precompose.navigation.Navigator
import moe.tlaster.precompose.navigation.PopUpTo
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import roar.sharedlib.generated.resources.Res
import roar.sharedlib.generated.resources.app_name
import roar.sharedlib.generated.resources.ic_care_filled
import roar.sharedlib.generated.resources.ic_community
import roar.sharedlib.generated.resources.ic_tab_home
import roar.sharedlib.generated.resources.next_button
import roar.sharedlib.generated.resources.onboarding_care_assistant
import roar.sharedlib.generated.resources.onboarding_care_reminders
import roar.sharedlib.generated.resources.onboarding_care_reminders_description
import roar.sharedlib.generated.resources.onboarding_community_project
import roar.sharedlib.generated.resources.onboarding_community_project_description
import roar.sharedlib.generated.resources.start

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnboardingRootScreen(
    navHostController: Navigator,
) {
    val state = rememberPagerState(pageCount = { return@rememberPagerState 3 })
    val scope = rememberCoroutineScope()

    val viewModel = koinViewModel(vmClass = OnboardingViewModel::class)

    SurfaceScaffold {
        Box {
            HorizontalPager(
                state = state,
                modifier = Modifier
                    .padding(WindowInsets.systemBars.asPaddingValues())
                    .padding(bottom = 72.dp)
            ) { page ->
                when (page) {
                    0 -> OnboardingHelloPage()
                    1 -> OnboardingCarePage()
                    2 -> OnboardingCommunityPage()
                }
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 16.dp)
            ) {
                DotsIndicator(
                    totalDots = 3,
                    selectedIndex = state.currentPage,
                    selectedColor = MaterialTheme.colorScheme.inverseSurface,
                    unSelectedColor = MaterialTheme.colorScheme.outline,
                )
                Spacer(size = 16.dp)
                if (state.currentPage < 2) {
                    PrimaryElevatedButton(
                        text = stringResource(resource = Res.string.next_button),
                        onClick = {
                            scope.launch {
                                state.animateScrollToPage(state.currentPage + 1)
                            }
                        })
                } else {
                    PrimaryElevatedButton(
                        text = stringResource(resource = Res.string.start),
                        onClick = {
                            viewModel.completeOnboarding()
                            navHostController.navigate(
                                NavigationKeys.Route.HOME_ROUTE, NavOptions(
                                    popUpTo = PopUpTo(
                                        route = NavigationKeys.Route.ONBOARDING_ROUTE,
                                        inclusive = true
                                    )
                                )
                            )
                        })
                }
                Spacer(
                    size = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
                )
            }
        }
    }
}

@Composable
fun OnboardingHelloPage() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Surface(
            tonalElevation = RoarTheme.IMAGE_ITEM_ELEVATION,
            modifier = Modifier
                .clip(MaterialTheme.shapes.extraLarge)
                .size(160.dp)
        ) {
            Image(
                painter = painterResource(resource = Res.drawable.ic_tab_home),
                contentDescription = "icon",
                modifier = Modifier.padding(16.dp),
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.inverseSurface)
            )
        }
        Spacer(size = 24.dp)
        Text(
            text = stringResource(resource = Res.string.app_name),
            style = MaterialTheme.typography.displayMedium
        )
        Text(
            text = stringResource(resource = Res.string.onboarding_care_assistant),
            style = MaterialTheme.typography.titleLarge
        )
    }
}

@Composable
fun OnboardingCarePage() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Surface(
            tonalElevation = RoarTheme.IMAGE_ITEM_ELEVATION,
            modifier = Modifier
                .clip(MaterialTheme.shapes.extraLarge)
                .size(160.dp)
        ) {
            Image(
                painter = painterResource(resource = Res.drawable.ic_care_filled),
                contentDescription = "icon",
                modifier = Modifier.padding(24.dp),
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.inverseSurface)
            )
        }
        Spacer(size = 24.dp)
        Text(
            text = stringResource(resource = Res.string.onboarding_care_reminders),
            style = MaterialTheme.typography.displaySmall
        )
        Spacer(size = 4.dp)
        Text(
            text = stringResource(resource = Res.string.onboarding_care_reminders_description),
            style = MaterialTheme.typography.titleMedium
        )
    }
}

@Composable
fun OnboardingCommunityPage() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Surface(
            tonalElevation = RoarTheme.IMAGE_ITEM_ELEVATION,
            modifier = Modifier
                .clip(MaterialTheme.shapes.extraLarge)
                .size(160.dp)
        ) {
            Image(
                painter = painterResource(resource = Res.drawable.ic_community),
                contentDescription = "icon",
                modifier = Modifier.padding(24.dp),
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.inverseSurface)
            )
        }
        Spacer(size = 24.dp)
        Text(
            text = stringResource(resource = Res.string.onboarding_community_project),
            style = MaterialTheme.typography.displaySmall
        )
        Spacer(size = 4.dp)
        Text(
            text = stringResource(resource = Res.string.onboarding_community_project_description),
            style = MaterialTheme.typography.titleMedium
        )
    }
}