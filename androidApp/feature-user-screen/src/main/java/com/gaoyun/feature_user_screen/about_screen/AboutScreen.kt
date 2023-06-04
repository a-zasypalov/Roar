package com.gaoyun.feature_user_screen.about_screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import com.gaoyun.common.composables.SurfaceScaffold
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
    val state = viewModel.viewState.collectAsState().value

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

    }
}