package com.gaoyun.feature_user_screen

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ListAlt
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gaoyun.common.R
import com.gaoyun.common.composables.AutoResizeText
import com.gaoyun.common.composables.BoxWithLoader
import com.gaoyun.common.composables.DropdownMenu
import com.gaoyun.common.composables.FontSizeRange
import com.gaoyun.common.composables.LabelledCheckBox
import com.gaoyun.common.composables.Spacer
import com.gaoyun.feature_user_screen.user_screen.UserScreenBackupBlock
import com.gaoyun.roar.model.domain.User
import com.gaoyun.roar.presentation.user_screen.UserScreenContract

@Composable
internal fun UserScreenContent(
    state: UserScreenContract.State,
    onCreateBackupClick: (UserScreenContract.Event.OnCreateBackupClick) -> Unit,
    onUseBackup: (UserScreenContract.Event.OnUseBackup) -> Unit,
    onNumberOfRemindersOnMainScreenChange: (UserScreenContract.Event.OnNumberOfRemindersOnMainScreen) -> Unit,
    onDynamicColorsStateChange: (UserScreenContract.Event.OnDynamicColorsStateChange) -> Unit,
    onLogout: (UserScreenContract.Event.OnLogout) -> Unit
) {
    val context = LocalContext.current
    val activity = LocalContext.current as? AppCompatActivity

    val numberOfRemindersOnMainScreenState = remember { mutableStateOf(state.numberOfRemindersOnMainScreenState) }
    numberOfRemindersOnMainScreenState.value = state.numberOfRemindersOnMainScreenState

    BoxWithLoader(isLoading = state.user == null) {
        state.user?.let { user ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 16.dp,
                        end = 16.dp,
                        top = 8.dp
                    )
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    AutoResizeText(
                        text = "Hey, ${user.name}",
                        maxLines = 2,
                        style = MaterialTheme.typography.displayMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSizeRange = FontSizeRange(
                            min = 20.sp,
                            max = MaterialTheme.typography.displayMedium.fontSize,
                        ),
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(0.8f)
                    )

                    Icon(
                        Icons.Default.Person, contentDescription = null, modifier = Modifier
                            .size(40.dp)
                            .padding(end = 8.dp, top = 8.dp)
                    )
                }

                Spacer(size = 8.dp)

                Text(
                    text = "Thank you for being with us",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(size = 32.dp)

                Text(
                    text = stringResource(id = R.string.backup),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(size = 8.dp)

                UserScreenBackupBlock(onCreateBackupClick, onUseBackup)

                Spacer(size = 32.dp)

                Text(
                    text = stringResource(id = R.string.app_settings),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(size = 8.dp)

                DropdownMenu(
                    valueList = listOf("0", "1", "2", "3", "4", "5"),
                    listState = numberOfRemindersOnMainScreenState,
                    onChange = { onNumberOfRemindersOnMainScreenChange(UserScreenContract.Event.OnNumberOfRemindersOnMainScreen(it.toIntOrNull() ?: 2)) },
                    label = stringResource(id = R.string.number_of_reminders_main_screen),
                    leadingIcon = Icons.Filled.ListAlt,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(size = 8.dp)

                LabelledCheckBox(
                    checked = state.dynamicColorActive,
                    onCheckedChange = {
                        onDynamicColorsStateChange(UserScreenContract.Event.OnDynamicColorsStateChange(it))
                        activity?.recreate()
                    },
                    label = stringResource(id = R.string.dynamic_color_switcher_title),
                    modifier = Modifier.fillMaxWidth(),
                    horizontalPadding = 8.dp
                )

                Spacer(size = 32.dp)

                TextButton(
                    onClick = {
                        AlertDialog.Builder(context)
                            .setTitle(context.getString(R.string.logout_question))
                            .setMessage(context.getString(R.string.logout_description))
                            .setPositiveButton(context.getString(R.string.logout)) { dialog, _ ->
                                dialog.dismiss()
                                onLogout(UserScreenContract.Event.OnLogout)
                            }
                            .setNegativeButton(context.getString(R.string.cancel)) { dialog, _ ->
                                dialog.dismiss()
                            }
                            .create()
                            .show()
                    },
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text(
                        text = stringResource(id = R.string.logout),
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(horizontal = 32.dp, vertical = 8.dp)
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun UserScreenPreview() {
    UserScreenContent(
        state = UserScreenContract.State(isLoading = false, dynamicColorActive = false, user = User("id", "Tester")),
        {}, {}, {}, {}, {}
    )
}