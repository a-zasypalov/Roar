package com.gaoyun.feature_create_reminder

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavHostController
import com.gaoyun.common.NavigationKeys
import com.gaoyun.common.OnLifecycleEvent
import com.gaoyun.common.ui.*
import com.gaoyun.roar.model.domain.interactions.InteractionTemplate
import com.gaoyun.roar.presentation.LAUNCH_LISTEN_FOR_EFFECTS
import com.gaoyun.roar.presentation.add_reminder.choose_template.AddReminderScreenContract
import com.gaoyun.roar.presentation.add_reminder.choose_template.AddReminderScreenViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.compose.getViewModel

@Composable
fun AddReminderDestination(
    navHostController: NavHostController,
    petId: String
) {
    val viewModel: AddReminderScreenViewModel = getViewModel()
    val state = viewModel.viewState.collectAsState().value

    OnLifecycleEvent { _, event ->
        if (event == Lifecycle.Event.ON_CREATE) {
            viewModel.buildScreenState(petId)
        }
    }

    AddReminderScreen(
        state = state,
        effectFlow = viewModel.effect,
        onEventSent = { event -> viewModel.setEvent(event) },
        onNavigationRequested = { navigationEffect ->
            when (navigationEffect) {
                is AddReminderScreenContract.Effect.Navigation.ToReminderSetup -> {
                    navHostController.navigate("${NavigationKeys.Route.ADD_REMINDER}/${navigationEffect.petId}/${navigationEffect.templateId}")
                }
                is AddReminderScreenContract.Effect.Navigation.NavigateBack -> navHostController.navigateUp()
            }
        },
    )

}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun AddReminderScreen(
    state: AddReminderScreenContract.State,
    effectFlow: Flow<AddReminderScreenContract.Effect>,
    onEventSent: (event: AddReminderScreenContract.Event) -> Unit,
    onNavigationRequested: (navigationEffect: AddReminderScreenContract.Effect.Navigation) -> Unit,
) {

    LaunchedEffect(LAUNCH_LISTEN_FOR_EFFECTS) {
        effectFlow.onEach { effect ->
            when (effect) {
                is AddReminderScreenContract.Effect.Navigation -> onNavigationRequested(effect)
                else -> {}
            }
        }.collect()
    }

    SurfaceScaffold {
        Box {
            LazyColumn {
                item {
                    Box(modifier = Modifier.size(WindowInsets.statusBars.asPaddingValues().calculateTopPadding()))
                }
                state.pet?.let { pet ->
                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 16.dp, top = 32.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                painter = painterResource(id = LocalContext.current.getDrawableByName(pet.avatar)),
                                contentDescription = pet.name,
                                modifier = Modifier.size(48.dp)
                            )

                            Spacer(size = 10.dp)

                            Text(
                                text = "Templates",
                                style = MaterialTheme.typography.displayMedium,
                            )
                        }
                    }

                    state.templates.groupBy { it.group }.forEach { (interactionGroup, templates) ->
                        item {
                            Text(
                                text = interactionGroup.toString(),
                                style = MaterialTheme.typography.headlineLarge,
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)
                            )
                        }

                        items(templates) { template ->
                            TemplateItem(
                                template = template,
                                onClick = { templateId ->
                                    onEventSent(AddReminderScreenContract.Event.TemplateChosen(templateId = templateId, petId = pet.id))
                                }
                            )
                        }
                    }

                    item {
                        Text(
                            text = "Custom",
                            style = MaterialTheme.typography.headlineLarge,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)
                        )
                    }

                    item {
                        Surface(
                            modifier = Modifier
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            shape = RoundedCornerShape(16.dp),
                            tonalElevation = 8.dp,
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        onEventSent(AddReminderScreenContract.Event.TemplateChosen(templateId = "null", petId = pet.id))
                                    }
                                    .padding(horizontal = 16.dp, vertical = 8.dp),
                            ) {
                                Text("Custom")
                                Text("Custom reminder setup")
                            }
                        }
                    }

                    item {
                        Spacer(size = 56.dp)
                    }
                }
            }
            Loader(isLoading = state.isLoading)
        }
    }

}

@Composable
private fun TemplateItem(
    template: InteractionTemplate,
    onClick: (String) -> Unit
) {
    Surface(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        tonalElevation = 8.dp,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick(template.id) }
                .padding(horizontal = 16.dp, vertical = 8.dp),
        ) {
            Text(template.name)
            Text(template.repeatConfig.toString())
        }
    }
}