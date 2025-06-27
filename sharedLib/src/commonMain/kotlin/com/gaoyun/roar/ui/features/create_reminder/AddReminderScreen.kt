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
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import com.gaoyun.roar.presentation.add_reminder.choose_template.AddReminderScreenContract
import com.gaoyun.roar.presentation.add_reminder.choose_template.AddReminderScreenViewModel
import com.gaoyun.roar.ui.common.composables.BoxWithLoader
import com.gaoyun.roar.ui.common.composables.PrimaryElevatedButtonOnSurface
import com.gaoyun.roar.ui.common.composables.SurfaceScaffold
import com.gaoyun.roar.ui.navigation.BackNavigationEffect
import com.gaoyun.roar.ui.navigation.NavigationSideEffect
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import roar.sharedlib.generated.resources.Res
import roar.sharedlib.generated.resources.done

@Composable
fun AddReminderDestination(
    navigate: (NavigationSideEffect) -> Unit,
    petId: String
) {
    val viewModel = koinViewModel<AddReminderScreenViewModel>()
    val state = viewModel.viewState.collectAsState().value

    LifecycleEventEffect(Lifecycle.Event.ON_CREATE) {
        viewModel.initialize(petId)
    }

    SurfaceScaffold(
        backHandler = { navigate(BackNavigationEffect) },
    ) {
        BoxWithLoader(isLoading = state.isLoading) {
            state.pet?.let { pet ->
                TemplatesList(
                    pet = pet,
                    templates = state.templates,
                    templateChosen = { template ->
                        navigate(AddReminderScreenContract.Effect.Navigation.ToReminderSetup(pet.id, template.templateId))
                    },
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
                            onClick = { navigate(BackNavigationEffect) },
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