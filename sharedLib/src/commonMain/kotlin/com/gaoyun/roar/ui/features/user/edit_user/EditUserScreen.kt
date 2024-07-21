package com.gaoyun.roar.ui.features.user.edit_user

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.gaoyun.roar.presentation.BackNavigationEffect
import com.gaoyun.roar.presentation.LAUNCH_LISTEN_FOR_EFFECTS
import com.gaoyun.roar.presentation.NavigationSideEffect
import com.gaoyun.roar.presentation.user_edit.EditUserScreenContract
import com.gaoyun.roar.presentation.user_edit.EditUserScreenViewModel
import com.gaoyun.roar.ui.common.composables.BoxWithLoader
import com.gaoyun.roar.ui.common.composables.SurfaceScaffold
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import moe.tlaster.precompose.koin.koinViewModel
import org.jetbrains.compose.resources.stringResource
import roar.sharedlib.generated.resources.Res
import roar.sharedlib.generated.resources.edit_profile

@Composable
fun EditUserScreenDestination(
    onNavigationCall: (NavigationSideEffect) -> Unit,
) {
    val viewModel = koinViewModel(vmClass = EditUserScreenViewModel::class)
    val state = viewModel.viewState.collectAsState().value

    LaunchedEffect(LAUNCH_LISTEN_FOR_EFFECTS) {
        viewModel.effect.onEach { effect ->
            when (effect) {
                is EditUserScreenContract.Effect.NavigateBack -> onNavigationCall(BackNavigationEffect)
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
                        text = stringResource(resource = Res.string.edit_profile),
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