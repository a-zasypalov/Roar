package com.gaoyun.roar.ui.features.user.edit_user

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.gaoyun.roar.presentation.user_edit.EditUserScreenViewModel
import com.gaoyun.roar.ui.common.composables.BoxWithLoader
import com.gaoyun.roar.ui.common.composables.SurfaceScaffold
import com.gaoyun.roar.ui.navigation.BackNavigationEffect
import com.gaoyun.roar.ui.navigation.NavigationSideEffect
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import roar.sharedlib.generated.resources.Res
import roar.sharedlib.generated.resources.edit_profile

@Composable
fun EditUserScreenDestination(
    navigate: (NavigationSideEffect) -> Unit,
) {
    val viewModel = koinViewModel<EditUserScreenViewModel>()
    val state by viewModel.viewState.collectAsState()

    SurfaceScaffold(
        backHandler = { navigate(BackNavigationEffect) }
    ) {
        BoxWithLoader(isLoading = state.isLoading) {
            state.userToEdit?.let { user ->
                Box(
                    contentAlignment = Alignment.BottomCenter,
                    modifier = Modifier.fillMaxSize()
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
                        onSaveClick = { updatedUser ->
                            viewModel.saveUser(updatedUser.user) {
                                navigate(BackNavigationEffect)
                            }
                        }
                    )
                }
            }
        }
    }
}