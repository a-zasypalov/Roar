package com.gaoyun.feature_onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.gaoyun.common.NavigationKeys
import com.gaoyun.common.R
import com.gaoyun.common.theme.RoarTheme
import com.gaoyun.common.ui.PrimaryElevatedButton
import com.gaoyun.common.ui.Spacer
import com.gaoyun.common.ui.SurfaceScaffold
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class, ExperimentalMaterial3Api::class)
@Composable
fun OnboardingRootScreen(
    navHostController: NavHostController,
) {
    val state = rememberPagerState()
    val scope = rememberCoroutineScope()

    SurfaceScaffold {
        Box {
            HorizontalPager(
                count = 3,
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
                        text = "Next",
                        onClick = {
                            scope.launch {
                                state.animateScrollToPage(state.currentPage + 1)
                            }
                        })
                } else {
                    PrimaryElevatedButton(
                        text = "Start",
                        onClick = {
                            navHostController.navigate(NavigationKeys.Route.HOME_ROUTE) {
                                popUpTo(NavigationKeys.Route.ONBOARDING_ROUTE) {
                                    inclusive = true
                                }
                            }
                        })
                }
                Spacer(size = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding())
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
            tonalElevation = 12.dp,
            modifier = Modifier
                .clip(MaterialTheme.shapes.extraLarge)
                .size(160.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_tab_home),
                contentDescription = "icon",
                modifier = Modifier.padding(16.dp),
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.inverseSurface)
            )
        }
        Spacer(size = 24.dp)
        Text(text = "Roar", style = MaterialTheme.typography.displayMedium)
        Text(text = "Pet's care assistant", style = MaterialTheme.typography.titleLarge)
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
            tonalElevation = 12.dp,
            modifier = Modifier
                .clip(MaterialTheme.shapes.extraLarge)
                .size(160.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_care_filled),
                contentDescription = "icon",
                modifier = Modifier.padding(24.dp),
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.inverseSurface)
            )
        }
        Spacer(size = 24.dp)
        Text(text = "Care reminders", style = MaterialTheme.typography.displaySmall)
        Spacer(size = 4.dp)
        Text(text = "Sometimes pets aren't talkative about needs", style = MaterialTheme.typography.titleMedium)
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
            tonalElevation = 12.dp,
            modifier = Modifier
                .clip(MaterialTheme.shapes.extraLarge)
                .size(160.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_community),
                contentDescription = "icon",
                modifier = Modifier.padding(24.dp),
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.inverseSurface)
            )
        }
        Spacer(size = 24.dp)
        Text(text = "Community project", style = MaterialTheme.typography.displaySmall)
        Spacer(size = 4.dp)
        Text(text = "Roar is a community-driven project", style = MaterialTheme.typography.titleMedium)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun OnboardingHelloPagePreview() {
    RoarTheme {
        SurfaceScaffold {
            OnboardingHelloPage()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun OnboardingCarePagePreview() {
    RoarTheme {
        SurfaceScaffold {
            OnboardingCarePage()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun OnboardingCommunityPagePreview() {
    RoarTheme {
        SurfaceScaffold {
            OnboardingCommunityPage()
        }
    }
}