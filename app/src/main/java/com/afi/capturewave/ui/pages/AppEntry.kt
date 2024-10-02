package com.afi.capturewave.ui.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.afi.capturewave.enums.RecorderType
import com.afi.capturewave.ui.common.Route
import com.afi.capturewave.ui.common.animatedComposable
import com.afi.capturewave.ui.pages.home.HomeScreen
import com.afi.capturewave.ui.pages.recordings.PlayerScreen
import com.afi.capturewave.ui.pages.settings.SettingsPage
import com.afi.capturewave.ui.pages.settings.about.AboutPage
import com.afi.capturewave.ui.pages.settings.about.CreditsPage
import com.afi.capturewave.ui.pages.settings.about.UpdatePage
import com.afi.capturewave.ui.pages.settings.appearance.AppearancePreferences
import com.afi.capturewave.ui.pages.settings.appearance.DarkThemePreferences
import com.afi.capturewave.ui.pages.settings.appearance.LanguagePage
import com.afi.capturewave.ui.pages.settings.audio_format.AudioFormatPage
import com.afi.capturewave.ui.pages.settings.general.GeneralPage
import com.afi.capturewave.ui.pages.settings.recorder.ScreenRecorderPage

@Composable
fun AppEntry(initialRecorder: RecorderType) {
    val navController = rememberNavController()

    val onNavigateBack: () -> Unit = {
        with(navController) {
            if (currentBackStackEntry?.lifecycle?.currentState == Lifecycle.State.RESUMED) {
                popBackStack()
            }
        }
    }
    Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        NavHost(
            modifier = Modifier.align(Alignment.Center),
            navController = navController,
            startDestination = Route.HOME,
        ) {
            animatedComposable(Route.HOME) {
                HomeScreen(initialRecorder, navController)
            }

            animatedComposable(Route.RECORDING_PLAYER) {
                PlayerScreen(false, onNavigateBack)
            }

            settingsGraph(
                onNavigateBack = onNavigateBack,
                onNavigateTo = { route ->
                    navController.navigate(route = route) { launchSingleTop = true }
                },
            )
        }

        AppUpdater()
    }
}

fun NavGraphBuilder.settingsGraph(
    onNavigateBack: () -> Unit,
    onNavigateTo: (route: String) -> Unit,
) {
    navigation(startDestination = Route.SETTINGS_PAGE, route = Route.SETTINGS) {
        animatedComposable(Route.SETTINGS_PAGE) {
            SettingsPage(onNavigateBack = onNavigateBack, onNavigateTo = onNavigateTo)
        }
        animatedComposable(Route.ABOUT) {
            AboutPage(onNavigateBack = onNavigateBack, onNavigateTo = onNavigateTo)
        }
        animatedComposable(Route.APPEARANCE) {
            AppearancePreferences(onNavigateBack = onNavigateBack, onNavigateTo = onNavigateTo)
        }
        animatedComposable(Route.GENERAL) { GeneralPage(onNavigateBack) }
        animatedComposable(Route.SCREEN_RECORDER) { ScreenRecorderPage(onNavigateBack) }
        animatedComposable(Route.AUDIO_FORMAT) { AudioFormatPage(onNavigateBack) }
        animatedComposable(Route.UPDATE) { UpdatePage(onNavigateBack) }
        animatedComposable(Route.CREDITS) { CreditsPage(onNavigateBack) }
        animatedComposable(Route.LANGUAGES) { LanguagePage { onNavigateBack() } }
        animatedComposable(Route.DARK_THEME) { DarkThemePreferences { onNavigateBack() } }
    }
}