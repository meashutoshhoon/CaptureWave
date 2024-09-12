package com.afi.capturewave.ui

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.afi.capturewave.R
import com.afi.capturewave.enums.RecorderType
import com.afi.capturewave.ui.common.Route
import com.afi.capturewave.ui.common.animatedComposable
import com.afi.capturewave.ui.pages.home.HomeScreen
import com.afi.capturewave.ui.pages.recordings.PlayerScreen
import com.afi.capturewave.ui.pages.settings.SettingsPage
import com.afi.capturewave.ui.pages.settings.about.AboutPage
import com.afi.capturewave.ui.pages.settings.about.CreditsPage
import com.afi.capturewave.ui.pages.settings.about.UpdatePage
import com.afi.capturewave.ui.pages.settings.audio_format.AudioFormatPage
import com.afi.capturewave.ui.pages.settings.general.GeneralPage
import com.afi.capturewave.ui.pages.settings.recorder.ScreenRecorderPage
import com.afi.capturewave.ui.pages.settings.theme.DarkThemePreferences
import com.afi.capturewave.util.PreferenceUtil
import com.afi.capturewave.util.ToastUtil
import com.afi.capturewave.util.UpdateUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@Composable
fun AppNavHost(
    initialRecorder: RecorderType
) {
    val navController = rememberNavController()
    val context = LocalContext.current
    var showUpdateDialog by rememberSaveable { mutableStateOf(false) }
    var currentDownloadStatus by remember { mutableStateOf(UpdateUtil.DownloadStatus.NotYet as UpdateUtil.DownloadStatus) }
    val scope = rememberCoroutineScope()
    var updateJob: Job? = null
    var latestRelease by remember { mutableStateOf(UpdateUtil.LatestRelease()) }
    val settings =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            UpdateUtil.installLatestApk()
        }
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { result ->
        if (result) {
            UpdateUtil.installLatestApk()
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                if (!context.packageManager.canRequestPackageInstalls())
                    settings.launch(
                        Intent(
                            Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES,
                            Uri.parse("package:${context.packageName}"),
                        )
                    )
                else
                    UpdateUtil.installLatestApk()
            }
        }
    }

    val onNavigateBack: () -> Unit = {
        with(navController) {
            if (currentBackStackEntry?.lifecycle?.currentState == Lifecycle.State.RESUMED) {
                popBackStack()
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        NavHost(
            navController = navController,
            startDestination = Route.HOME,
            modifier = Modifier
        ) {
            animatedComposable(Route.HOME) {
                HomeScreen(initialRecorder, navController)
            }

            animatedComposable(Route.RECORDING_PLAYER) {
                PlayerScreen(false, onNavigateBack)
            }

            settingsGraph(
                navController = navController, onNavigateBack = onNavigateBack
            )
        }

        LaunchedEffect(Unit) {
            if (!PreferenceUtil.isNetworkAvailableForDownload() || !PreferenceUtil.isAutoUpdateEnabled()
            )
                return@LaunchedEffect
            launch(Dispatchers.IO) {
                runCatching {
                    UpdateUtil.checkForUpdate()?.let {
                        latestRelease = it
                        showUpdateDialog = true
                    }
                }.onFailure {
                    it.printStackTrace()
                }
            }
        }

        if (showUpdateDialog) {
            UpdateDialogImpl(
                onDismissRequest = {
                    showUpdateDialog = false
                    updateJob?.cancel()
                },
                title = latestRelease.name.toString(),
                onConfirmUpdate = {
                    updateJob = scope.launch(Dispatchers.IO) {
                        runCatching {
                            UpdateUtil.downloadApk(latestRelease = latestRelease)
                                .collect { downloadStatus ->
                                    currentDownloadStatus = downloadStatus
                                    if (downloadStatus is UpdateUtil.DownloadStatus.Finished) {
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                            launcher.launch(Manifest.permission.REQUEST_INSTALL_PACKAGES)
                                        }
                                    }
                                }
                        }.onFailure {
                            it.printStackTrace()
                            currentDownloadStatus = UpdateUtil.DownloadStatus.NotYet
                            ToastUtil.makeToastSuspend(context.getString(R.string.app_update_failed))
                            return@launch
                        }
                    }
                },
                releaseNote = latestRelease.body.toString(),
                downloadStatus = currentDownloadStatus
            )
        }
    }
}

fun NavGraphBuilder.settingsGraph(
    navController: NavHostController, onNavigateBack: () -> Unit
) {
    navigation(startDestination = Route.SETTINGS_PAGE, route = Route.SETTINGS) {

        animatedComposable(Route.SETTINGS_PAGE) {
            SettingsPage(
                navController = navController, onNavigateBack = onNavigateBack
            )
        }
        animatedComposable(Route.ABOUT) {
            AboutPage(
                onNavigateBack = onNavigateBack, navController = navController
            )
        }
        animatedComposable(Route.GENERAL) { GeneralPage(onNavigateBack) }
        animatedComposable(Route.SCREEN_RECORDER) { ScreenRecorderPage(onNavigateBack) }
        animatedComposable(Route.AUDIO_FORMAT) { AudioFormatPage(onNavigateBack) }
        animatedComposable(Route.UPDATE) { UpdatePage(onNavigateBack) }
        animatedComposable(Route.CREDITS) { CreditsPage(onNavigateBack) }
        animatedComposable(Route.THEME) { DarkThemePreferences(onNavigateBack) }
    }
}