package com.gaoyun.roar.ui.features.create_reminder

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.gaoyun.roar.presentation.BackNavigationEffect
import com.gaoyun.roar.presentation.LAUNCH_LISTEN_FOR_EFFECTS
import com.gaoyun.roar.presentation.NavigationSideEffect
import com.gaoyun.roar.presentation.add_reminder.choose_template.AddReminderScreenContract
import com.gaoyun.roar.presentation.add_reminder.choose_template.AddReminderScreenViewModel
import com.gaoyun.roar.ui.common.composables.BoxWithLoader
import com.gaoyun.roar.ui.common.composables.PrimaryElevatedButtonOnSurface
import com.gaoyun.roar.ui.common.composables.SurfaceScaffold
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import moe.tlaster.precompose.koin.koinViewModel
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource
import roar.sharedlib.generated.resources.Res
import roar.sharedlib.generated.resources.done

@OptIn(ExperimentalResourceApi::class)
@Composable
fun AddReminderDestination(
    onNavigationCall: (NavigationSideEffect) -> Unit,
    petId: String
) {
    val viewModel = koinViewModel(vmClass = AddReminderScreenViewModel::class)
    val state = viewModel.viewState.collectAsState().value

    LaunchedEffect(Unit) {
        //TODO: was on onCreate
        viewModel.buildScreenState(petId)
    }

    LaunchedEffect(LAUNCH_LISTEN_FOR_EFFECTS) {
        viewModel.effect.onEach { effect ->
            when (effect) {
                is AddReminderScreenContract.Effect.Navigation -> onNavigationCall(effect)
            }
        }.collect()
    }

    SurfaceScaffold(
        backHandler = { onNavigationCall(BackNavigationEffect) },
    ) {
        BoxWithLoader(isLoading = state.isLoading) {
            state.pet?.let {
                TemplatesList(
                    pet = it,
                    templates = state.templates,
                    templateChosen = viewModel::setEvent,
                    modifier = Modifier
                        .navigationBarsPadding()
                        .padding(bottom = 56.dp)
                )
                Box(modifier = Modifier.fillMaxSize()) {
                    Surface(
                        tonalElevation = 120.dp,
                        shadowElevation = 12.dp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(
                                88.dp + ScaffoldDefaults.contentWindowInsets
                                    .asPaddingValues()
                                    .calculateBottomPadding()
                            )
                            .align(Alignment.BottomCenter)
                    ) {
                        PrimaryElevatedButtonOnSurface(
                            text = stringResource(resource = Res.string.done),
                            onClick = { onNavigationCall(BackNavigationEffect) },
                            modifier = Modifier
                                .align(Alignment.Center)
                                .padding(vertical = 16.dp)
                                .navigationBarsPadding()
                        )
                    }

                }
            }
        }
    }
}