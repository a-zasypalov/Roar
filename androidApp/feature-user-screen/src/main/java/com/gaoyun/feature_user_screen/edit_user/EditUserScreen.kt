package com.gaoyun.feature_user_screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.gaoyun.common.R
import com.gaoyun.common.composables.*
import com.gaoyun.roar.presentation.LAUNCH_LISTEN_FOR_EFFECTS
import com.gaoyun.roar.presentation.user_edit.EditUserScreenContract
import com.gaoyun.roar.presentation.user_edit.EditUserScreenViewModel
import kotlinx.coroutines.flow.*
import org.koin.androidx.compose.getViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditUserScreenDestination(
    navHostController: NavHostController,
) {
    val viewModel: EditUserScreenViewModel = getViewModel()
    val state = viewModel.viewState.collectAsState().value

    LaunchedEffect(LAUNCH_LISTEN_FOR_EFFECTS) {
        viewModel.effect.onEach { effect ->
            when (effect) {
                is EditUserScreenContract.Effect.Navigation.NavigateBack -> navHostController.navigateUp()
            }
        }.collect()
    }

    SurfaceScaffold(
        backHandler = { viewModel.setEvent(EditUserScreenContract.Event.NavigateBack) },
    ) {
        BoxWithLoader(isLoading = state.userToEdit == null) {
            state.userToEdit?.let { user ->
                Box(
                    contentAlignment = Alignment.BottomCenter, modifier = Modifier.fillMaxSize()
                ) {
                    Text(
                        text = stringResource(id = R.string.edit_profile),
                        style = MaterialTheme.typography.displayMedium,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(start = 10.dp, top = 8.dp, bottom = 16.dp),
                    )
                    EditUserForm(
                        user = user,
                        onSaveClick = viewModel::setEvent,
                    )
                }
            }
        }
    }
}