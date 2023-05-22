package com.gaoyun.feature_create_reminder

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import com.gaoyun.common.OnLifecycleEvent
import com.gaoyun.common.R
import com.gaoyun.common.composables.*
import com.gaoyun.roar.presentation.BackNavigationEffect
import com.gaoyun.roar.presentation.LAUNCH_LISTEN_FOR_EFFECTS
import com.gaoyun.roar.presentation.NavigationSideEffect
import com.gaoyun.roar.presentation.add_reminder.choose_template.AddReminderScreenContract
import com.gaoyun.roar.presentation.add_reminder.choose_template.AddReminderScreenViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.compose.getViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddReminderDestination(
    onNavigationCall: (NavigationSideEffect) -> Unit,
    petId: String
) {
    val viewModel: AddReminderScreenViewModel = getViewModel()
    val state = viewModel.viewState.collectAsState().value

    OnLifecycleEvent { _, event ->
        if (event == Lifecycle.Event.ON_CREATE) {
            viewModel.buildScreenState(petId)
        }
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
                            text = stringResource(id = R.string.done),
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