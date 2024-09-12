package com.afi.capturewave.ui.pages.home

import androidx.activity.ComponentActivity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Subscriptions
import androidx.compose.material.icons.rounded.Mic
import androidx.compose.material.icons.rounded.Videocam
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.afi.capturewave.R
import com.afi.capturewave.enums.RecorderState
import com.afi.capturewave.enums.RecorderType
import com.afi.capturewave.ui.common.HapticFeedback.slightHapticFeedback
import com.afi.capturewave.ui.common.Route
import com.afi.capturewave.ui.models.RecorderModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    initialRecorder: RecorderType,
    navController: NavController,
    recorderModel: RecorderModel = viewModel(LocalContext.current as ComponentActivity)
) {
    val pagerState = rememberPagerState { 2 }
    val scope = rememberCoroutineScope()
    val view = LocalView.current

    Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
        TopAppBar(title = {}, actions = {
            TooltipBox(state = rememberTooltipState(),
                positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
                tooltip = {
                    PlainTooltip {
                        Text(text = stringResource(id = R.string.recordings))
                    }
                }) {
                IconButton(
                    onClick = {
                        view.slightHapticFeedback()
                        navController.navigate(Route.RECORDING_PLAYER) {
                            launchSingleTop = true
                            restoreState = true
                        }
                    }, modifier = Modifier
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Subscriptions,
                        contentDescription = stringResource(id = R.string.recordings)
                    )
                }
            }
        }, navigationIcon = {
            TooltipBox(state = rememberTooltipState(),
                positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
                tooltip = {
                    PlainTooltip {
                        Text(text = stringResource(id = R.string.settings))
                    }
                }) {
                IconButton(
                    onClick = {
                        view.slightHapticFeedback()
                        navController.navigate(Route.SETTINGS) {
                            launchSingleTop = true
                            restoreState = true
                        }
                    }, modifier = Modifier
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Settings,
                        contentDescription = stringResource(id = R.string.settings)
                    )
                }
            }
        })
    }, bottomBar = {
        Column {
            AnimatedVisibility(recorderModel.recorderState == RecorderState.IDLE) {
                NavigationBar {
                    NavigationBarItem(
                        icon = {
                            Icon(
                                imageVector = Icons.Rounded.Mic,
                                contentDescription = stringResource(
                                    id = R.string.record_sound
                                )
                            )
                        },
                        label = { Text(stringResource(R.string.record_sound)) },
                        selected = (pagerState.currentPage == 0),
                        onClick = {
                            scope.launch {
                                pagerState.animateScrollToPage(0)
                            }
                        }
                    )
                    NavigationBarItem(
                        icon = {
                            Icon(
                                imageVector = Icons.Rounded.Videocam,
                                contentDescription = stringResource(
                                    id = R.string.record_screen
                                )
                            )
                        },
                        label = { Text(stringResource(R.string.record_screen)) },
                        selected = (pagerState.currentPage == 1),
                        onClick = {
                            scope.launch {
                                pagerState.animateScrollToPage(1)
                            }
                        }
                    )
                }
            }
        }
    }) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { index ->
                RecorderView(recordScreenMode = (index == 1))
            }
        }
    }
}