package com.gaoyun.feature_create_reminder

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavHostController
import com.gaoyun.common.OnLifecycleEvent
import com.gaoyun.common.R
import com.gaoyun.common.ui.Loader
import com.gaoyun.roar.model.domain.PetType
import com.gaoyun.roar.presentation.LAUNCH_LISTEN_FOR_EFFECTS
import com.gaoyun.roar.presentation.add_reminder.AddReminderScreenContract
import com.gaoyun.roar.presentation.add_reminder.AddReminderScreenViewModel
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
                is AddReminderScreenContract.Effect.Navigation.NavigateBack -> navHostController.navigateUp()
            }
        },
    )

}

@Composable
fun AddReminderScreen(
    state: AddReminderScreenContract.State,
    effectFlow: Flow<AddReminderScreenContract.Effect>,
    onEventSent: (event: AddReminderScreenContract.Event) -> Unit,
    onNavigationRequested: (navigationEffect: AddReminderScreenContract.Effect.Navigation) -> Unit,
) {

    LaunchedEffect(LAUNCH_LISTEN_FOR_EFFECTS) {
        effectFlow.onEach { effect ->
            when (effect) {
//                is AddReminderScreenContract.Effect.PetAdded -> {
//                    onNavigationRequested(AddPetScreenContract.Effect.Navigation.NavigateBack)
//                }
                else -> {}
            }
        }.collect()
    }

    val rememberedState = rememberScaffoldState()

    Scaffold(scaffoldState = rememberedState) {
        Box {
            LazyColumn {
                state.pet?.let { pet ->
                    item {
                        Card(
                            modifier = Modifier
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            shape = RoundedCornerShape(16.dp), elevation = 8.dp
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                when (pet.petType) {
                                    PetType.CAT -> {
                                        Image(
                                            painter = painterResource(id = R.drawable.ic_cat),
                                            contentDescription = "cat",
                                            colorFilter = ColorFilter.tint(color = MaterialTheme.colors.onSurface),
                                            modifier = Modifier.size(42.dp)
                                        )
                                    }
                                    PetType.DOG -> {
                                        Image(
                                            painter = painterResource(id = R.drawable.ic_dog),
                                            contentDescription = "dog",
                                            colorFilter = ColorFilter.tint(color = MaterialTheme.colors.onSurface),
                                            modifier = Modifier.size(42.dp)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.size(8.dp))

                                Text(pet.name, color = MaterialTheme.colors.onSurface)
                            }
                        }
                    }
                }

                items(state.templates) { template ->
                    Card(
                        modifier = Modifier
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        shape = RoundedCornerShape(16.dp), elevation = 8.dp
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                        ) {
                            Text(template.name)
                            Text(template.group.toString())
                            Text(template.repeatConfig.toString())
                        }
                    }
                }
            }

            Loader(isLoading = state.isLoading)
        }
    }

}