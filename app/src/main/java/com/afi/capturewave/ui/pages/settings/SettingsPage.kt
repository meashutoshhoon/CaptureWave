package com.afi.capturewave.ui.pages.settings

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ScreenShare
import androidx.compose.material.icons.rounded.AudioFile
import androidx.compose.material.icons.rounded.DarkMode
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.LightMode
import androidx.compose.material.icons.rounded.SettingsApplications
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.afi.capturewave.R
import com.afi.capturewave.enums.ThemeMode
import com.afi.capturewave.ui.common.Route
import com.afi.capturewave.ui.component.BackButton
import com.afi.capturewave.ui.component.SettingItem
import com.afi.capturewave.ui.models.ThemeModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsPage(
    navController: NavController, onNavigateBack: () -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val typography = MaterialTheme.typography
    val themeModel: ThemeModel = viewModel(LocalContext.current as ComponentActivity)

    Scaffold(modifier = Modifier
        .fillMaxSize()
        .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            val overrideTypography = remember(typography) {
                typography.copy(headlineMedium = typography.displaySmall)
            }
            MaterialTheme(typography = overrideTypography) {
                LargeTopAppBar(
                    title = {
                        Text(
                            text = stringResource(id = R.string.settings),
                        )
                    },
                    navigationIcon = { BackButton(onNavigateBack) },
                    scrollBehavior = scrollBehavior,
                    expandedHeight = TopAppBarDefaults.LargeAppBarExpandedHeight + 24.dp
                )
            }
        }) {
        LazyColumn(
            modifier = Modifier, contentPadding = it
        ) {
            item {
                SettingItem(
                    title = stringResource(id = R.string.general_settings),
                    description = stringResource(
                        id = R.string.general_settings_desc
                    ),
                    icon = Icons.Rounded.SettingsApplications
                ) {
                    navController.navigate(Route.GENERAL) {
                        launchSingleTop = true
                    }
                }
            }
            item {
                SettingItem(
                    title = stringResource(id = R.string.audio_format),
                    description = stringResource(id = R.string.audio_format_desc),
                    icon = Icons.Rounded.AudioFile
                ) {
                    navController.navigate(Route.AUDIO_FORMAT) {
                        launchSingleTop = true
                    }
                }
            }
            item {
                SettingItem(
                    title = stringResource(id = R.string.screen_recorder),
                    description = stringResource(id = R.string.screen_recorder_desc),
                    icon = Icons.AutoMirrored.Rounded.ScreenShare
                ) {
                    navController.navigate(Route.SCREEN_RECORDER) {
                        launchSingleTop = true
                    }
                }
            }
            item {
                val isDarkTheme = themeModel.themeMode == ThemeMode.DARK
                SettingItem(
                    title = stringResource(id = R.string.theme), description = stringResource(
                        id = R.string.theme_settings
                    ), icon = if (isDarkTheme) Icons.Rounded.DarkMode else Icons.Rounded.LightMode
                ) {
                    navController.navigate(Route.THEME) { launchSingleTop = true }
                }
            }
            item {
                SettingItem(
                    title = stringResource(id = R.string.about), description = stringResource(
                        id = R.string.about_page
                    ), icon = Icons.Rounded.Info
                ) {
                    navController.navigate(Route.ABOUT) { launchSingleTop = true }
                }
            }
        }
    }
}