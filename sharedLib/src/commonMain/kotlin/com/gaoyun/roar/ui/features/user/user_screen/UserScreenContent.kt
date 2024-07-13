package com.gaoyun.roar.ui.features.user.user_screen

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ListAlt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gaoyun.roar.model.domain.User
import com.gaoyun.roar.presentation.user_screen.UserScreenContract
import com.gaoyun.roar.ui.common.composables.AutoResizeText
import com.gaoyun.roar.ui.common.composables.BoxWithLoader
import com.gaoyun.roar.ui.common.composables.DropdownMenu
import com.gaoyun.roar.ui.common.composables.FontSizeRange
import com.gaoyun.roar.ui.common.composables.LabelledCheckBox
import com.gaoyun.roar.ui.common.composables.Spacer
import com.gaoyun.roar.ui.theme.primaryColor
import com.gaoyun.roar.util.AppIcon
import com.gaoyun.roar.util.ColorTheme
import com.gaoyun.roar.util.Platform
import com.gaoyun.roar.util.PlatformNames
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import roar.sharedlib.generated.resources.Res
import roar.sharedlib.generated.resources.about_app_button
import roar.sharedlib.generated.resources.app_icon
import roar.sharedlib.generated.resources.app_settings
import roar.sharedlib.generated.resources.backup
import roar.sharedlib.generated.resources.cancel
import roar.sharedlib.generated.resources.colors
import roar.sharedlib.generated.resources.dynamic_color_switcher_title
import roar.sharedlib.generated.resources.hey_user
import roar.sharedlib.generated.resources.home_screen_mode_switcher_title
import roar.sharedlib.generated.resources.ic_launcher_foreground
import roar.sharedlib.generated.resources.ic_launcher_paw_foreground
import roar.sharedlib.generated.resources.logout
import roar.sharedlib.generated.resources.logout_description
import roar.sharedlib.generated.resources.logout_question
import roar.sharedlib.generated.resources.number_of_reminders_main_screen
import roar.sharedlib.generated.resources.user_screen_subtitle

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
    onAboutScreenButtonClick: (UserScreenContract.Event.OnAboutScreenClick) -> Unit,
    onIconChange: (UserScreenContract.Event.OnAppIconChange) -> Unit
) {
    val isDarkTheme = isSystemInDarkTheme()
    val scrollState = rememberScrollState()
    val showLogoutDialog = remember { mutableStateOf(false) }

    val supportDynamicColor = Platform.supportsDynamicColor
    val numberOfRemindersOnMainScreenState = remember { mutableStateOf(state.numberOfRemindersOnMainScreenState) }
    numberOfRemindersOnMainScreenState.value = state.numberOfRemindersOnMainScreenState

    if (showLogoutDialog.value) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog.value = false },
            title = { Text(stringResource(resource = Res.string.logout_question)) },
            text = { Text(stringResource(resource = Res.string.logout_description)) },
            confirmButton = {
                TextButton(onClick = {
                    showLogoutDialog.value = false
                    onLogout(UserScreenContract.Event.OnLogout)
                }) {
                    Text(stringResource(resource = Res.string.logout))
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showLogoutDialog.value = false
                }) {
                    Text(stringResource(resource = Res.string.cancel))
                }
            }
        )
    }

    BoxWithLoader(isLoading = state.user == null) {
        state.user?.let { user ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(scrollState)
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
                        text = stringResource(resource = Res.string.hey_user, user.name),
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
                    text = stringResource(resource = Res.string.user_screen_subtitle),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(size = 16.dp)

                HorizontalDivider()

                Spacer(size = 16.dp)

                Text(
                    text = stringResource(resource = Res.string.app_settings),
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(size = 16.dp)

                if (state.numberOfPets == 1) {
                    LabelledCheckBox(
                        checked = state.screenModeFull,
                        onCheckedChange = {
                            onHomeScreenModeChange(
                                UserScreenContract.Event.OnHomeScreenModeChange(
                                    it
                                )
                            )
                        },
                        label = stringResource(resource = Res.string.home_screen_mode_switcher_title),
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
                        onChange = {
                            onNumberOfRemindersOnMainScreenChange(
                                UserScreenContract.Event.OnNumberOfRemindersOnMainScreen(
                                    it.toIntOrNull() ?: 2
                                )
                            )
                        },
                        label = stringResource(resource = Res.string.number_of_reminders_main_screen),
                        leadingIcon = Icons.AutoMirrored.Filled.ListAlt,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Spacer(size = 16.dp)

                HorizontalDivider()

                Spacer(size = 16.dp)

                Text(
                    text = stringResource(resource = Res.string.colors),
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Medium),
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(size = 4.dp)

                if (supportDynamicColor) {
                    LabelledCheckBox(
                        checked = state.dynamicColorActive,
                        onCheckedChange = {
                            onDynamicColorsStateChange(
                                UserScreenContract.Event.OnDynamicColorsStateChange(it)
                            )
                        },
                        label = stringResource(resource = Res.string.dynamic_color_switcher_title),
                        modifier = Modifier.fillMaxWidth(),
                        horizontalPadding = 8.dp
                    )
                }

                AnimatedVisibility(visible = !supportDynamicColor || !state.dynamicColorActive) {
                    LazyRow {
                        items(ColorTheme.entries.toTypedArray()) {
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
                                            onStaticColorThemePick(
                                                UserScreenContract.Event.OnStaticColorThemePick(
                                                    it
                                                )
                                            )
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

                HorizontalDivider()

                Spacer(size = 16.dp)

                Text(
                    text = stringResource(resource = Res.string.app_icon),
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Medium),
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
                                    .clickable { onIconChange(UserScreenContract.Event.OnAppIconChange(AppIcon.Roar)) }
                                    .clip(MaterialTheme.shapes.medium)
                            ) {
                                Image(
                                    painterResource(Res.drawable.ic_launcher_foreground),
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
                                    .clickable { onIconChange(UserScreenContract.Event.OnAppIconChange(AppIcon.Paw)) }
                                    .clip(MaterialTheme.shapes.medium)
                            ) {
                                Image(
                                    painterResource(Res.drawable.ic_launcher_paw_foreground),
                                    contentDescription = null,
                                    contentScale = ContentScale.FillWidth,
                                )
                            }
                        }
                    }
                }

                Spacer(size = 16.dp)

                //File backup currently supported only by Android
                if (Platform.name == PlatformNames.Android) {
                    HorizontalDivider()

                    Spacer(size = 16.dp)

                    Text(
                        text = stringResource(resource = Res.string.backup),
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Medium),
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(size = 8.dp)

                    UserScreenBackupBlock(onCreateBackupClick, onUseBackup)

                    Spacer(size = 16.dp)
                }

                HorizontalDivider()

                Spacer(size = 16.dp)

                TextButton(
                    onClick = { onAboutScreenButtonClick(UserScreenContract.Event.OnAboutScreenClick) },
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text(
                        text = stringResource(resource = Res.string.about_app_button),
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(horizontal = 32.dp, vertical = 8.dp)
                    )
                }

                TextButton(
                    onClick = {
                        showLogoutDialog.value = true
                    },
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text(
                        text = stringResource(resource = Res.string.logout),
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(horizontal = 32.dp, vertical = 8.dp)
                    )
                }

                Spacer(size = 96.dp)
            }
        }
    }
}

@Preview
@Composable
fun UserScreenPreview() {
    UserScreenContent(
        state = UserScreenContract.State(
            isLoading = false,
            dynamicColorActive = false,
            user = User("id", "Tester")
        ),
        {}, {}, {}, {}, {}, {}, {}, {}, {}
    )
}