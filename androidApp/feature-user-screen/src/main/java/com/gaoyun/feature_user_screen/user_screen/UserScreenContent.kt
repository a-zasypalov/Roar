package com.gaoyun.feature_user_screen.user_screen

import android.app.Activity
import android.app.AlertDialog
import android.content.ComponentName
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ListAlt
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
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
import com.gaoyun.common.theme.primaryColor
import com.gaoyun.roar.model.domain.User
import com.gaoyun.roar.presentation.user_screen.UserScreenContract
import com.gaoyun.roar.util.ColorTheme

@Composable
internal fun UserScreenContent(
    state: UserScreenContract.State,
    onCreateBackupClick: (UserScreenContract.Event.OnCreateBackupClick) -> Unit,
    onUseBackup: (UserScreenContract.Event.OnUseBackup) -> Unit,
    onNumberOfRemindersOnMainScreenChange: (UserScreenContract.Event.OnNumberOfRemindersOnMainScreen) -> Unit,
    onDynamicColorsStateChange: (UserScreenContract.Event.OnDynamicColorsStateChange) -> Unit,
    onHomeScreenModeChange: (UserScreenContract.Event.OnHomeScreenModeChange) -> Unit,
    onStaticColorThemePick: (UserScreenContract.Event.OnStaticColorThemePick) -> Unit,
    onLogout: (UserScreenContract.Event.OnLogout) -> Unit,
    onAboutScreenButtonClick: (UserScreenContract.Event.OnAboutScreenClick) -> Unit
) {
    val context = LocalContext.current
    val activity = LocalContext.current as? AppCompatActivity
    val isDarkTheme = isSystemInDarkTheme()

    val supportDynamicColor = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
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
                        text = stringResource(id = R.string.hey_user, user.name),
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
                    text = stringResource(id = R.string.user_screen_subtitle),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(size = 16.dp)

                Divider()

                Spacer(size = 16.dp)

                Text(
                    text = stringResource(id = R.string.app_settings),
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(size = 16.dp)

                if (state.numberOfPets == 1) {
                    LabelledCheckBox(
                        checked = state.screenModeFull,
                        onCheckedChange = {
                            onHomeScreenModeChange(UserScreenContract.Event.OnHomeScreenModeChange(it))
                        },
                        label = stringResource(id = R.string.home_screen_mode_switcher_title),
                        modifier = Modifier.fillMaxWidth(),
                        horizontalPadding = 8.dp
                    )
                }

                if ((state.numberOfPets == 1 && !state.screenModeFull) || state.numberOfPets > 1) {
                    if (state.numberOfPets == 1) {
                        Spacer(size = 16.dp)
                    }

                    DropdownMenu(
                        valueList = listOf("0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"),
                        listState = numberOfRemindersOnMainScreenState,
                        onChange = { onNumberOfRemindersOnMainScreenChange(UserScreenContract.Event.OnNumberOfRemindersOnMainScreen(it.toIntOrNull() ?: 2)) },
                        label = stringResource(id = R.string.number_of_reminders_main_screen),
                        leadingIcon = Icons.Filled.ListAlt,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Spacer(size = 16.dp)

                Divider()

                Spacer(size = 16.dp)

                Text(
                    text = stringResource(id = R.string.colors),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(size = 4.dp)

                if (supportDynamicColor) {
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
                }

                AnimatedVisibility(visible = !supportDynamicColor || !state.dynamicColorActive) {
                    LazyRow {
                        items(ColorTheme.values()) {
                            ElevatedCard(
                                shape = MaterialTheme.shapes.medium,
                                colors = CardDefaults.elevatedCardColors(
                                    containerColor = Color.White
                                ),
                                modifier = Modifier
                                    .padding(horizontal = 8.dp)
                                    .size(72.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .clickable {
                                            onStaticColorThemePick(UserScreenContract.Event.OnStaticColorThemePick(it))
                                            activity?.recreate()
                                        }
                                        .padding(8.dp)
                                        .clip(MaterialTheme.shapes.medium)
                                        .background(it.primaryColor(isDarkTheme))
                                ) {
                                    if (state.activeColorTheme == it) {
                                        Icon(
                                            Icons.Default.Check,
                                            contentDescription = null,
                                            tint = Color.White,
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .padding(12.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(size = 16.dp)

                Divider()

                Spacer(size = 16.dp)

                Text(
                    text = stringResource(id = R.string.app_icon),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(size = 16.dp)

                LazyRow {
                    item {
                        ElevatedCard(
                            shape = MaterialTheme.shapes.medium,
                            colors = CardDefaults.elevatedCardColors(
                                containerColor = Color.White
                            ),
                            modifier = Modifier
                                .padding(horizontal = 8.dp)
                                .size(72.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clickable {
                                        activity?.deactivateComponent(PAW_ICON)
                                        activity?.activateComponent(ROAR_ICON)
                                    }
                                    .clip(MaterialTheme.shapes.medium)
                            ) {
                                Image(
                                    painterResource(id = R.mipmap.ic_launcher_foreground),
                                    contentDescription = null,
                                    contentScale = ContentScale.FillWidth,
                                )
                            }
                        }
                    }
                    item {
                        ElevatedCard(
                            shape = MaterialTheme.shapes.medium,
                            colors = CardDefaults.elevatedCardColors(
                                containerColor = Color.White
                            ),
                            modifier = Modifier
                                .padding(horizontal = 8.dp)
                                .size(72.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clickable {
                                        activity?.deactivateComponent(ROAR_ICON)
                                        activity?.activateComponent(PAW_ICON)
                                    }
                                    .clip(MaterialTheme.shapes.medium)
                            ) {
                                Image(
                                    painterResource(id = R.mipmap.ic_launcher_paw_foreground),
                                    contentDescription = null,
                                    contentScale = ContentScale.FillWidth,
                                )
                            }
                        }
                    }
                }

                Spacer(size = 16.dp)

                Divider()

                Spacer(size = 16.dp)

                Text(
                    text = stringResource(id = R.string.backup),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(size = 8.dp)

                UserScreenBackupBlock(onCreateBackupClick, onUseBackup)

                Spacer(size = 16.dp)

                Divider()

                Spacer(size = 16.dp)

                TextButton(
                    onClick = { onAboutScreenButtonClick(UserScreenContract.Event.OnAboutScreenClick) },
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text(
                        text = stringResource(id = R.string.about_app_button),
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(horizontal = 32.dp, vertical = 8.dp)
                    )
                }

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

private fun Activity.activateComponent(name: String) {
    packageManager.setComponentEnabledSetting(
        ComponentName(this, name),
        PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP
    )
}

private fun Activity.deactivateComponent(name: String) {
    packageManager.setComponentEnabledSetting(
        ComponentName(this, name),
        PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP
    )
}

const val ROAR_ICON = "com.gaoyun.roar.android.ROAR.AMBER"
const val PAW_ICON = "com.gaoyun.roar.android.PAW.AMBER"

@Preview
@Composable
fun UserScreenPreview() {
    UserScreenContent(
        state = UserScreenContract.State(isLoading = false, dynamicColorActive = false, user = User("id", "Tester")),
        {}, {}, {}, {}, {}, {}, {}, {}
    )
}