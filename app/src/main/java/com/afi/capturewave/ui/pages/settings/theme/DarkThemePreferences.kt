package com.afi.capturewave.ui.pages.settings.theme

import android.os.Build
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.afi.capturewave.App.Companion.applicationScope
import com.afi.capturewave.R
import com.afi.capturewave.enums.ThemeMode
import com.afi.capturewave.ui.component.BackButton
import com.afi.capturewave.ui.component.PreferenceSingleChoiceItem
import com.afi.capturewave.ui.models.ThemeModel
import com.afi.capturewave.util.Preferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DarkThemePreferences(onNavigateBack: () -> Unit) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(
        rememberTopAppBarState(),
        canScroll = { true }
    )
    val themeModel: ThemeModel = viewModel(LocalContext.current as ComponentActivity)
    val isHighContrastModeEnabled = themeModel.themeMode == ThemeMode.AMOLED

    Scaffold(modifier = Modifier
        .fillMaxSize()
        .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            LargeTopAppBar(title = {
                Text(
                    modifier = Modifier,
                    text = stringResource(id = R.string.dark_theme),
                )
            }, navigationIcon = {
                BackButton {
                    onNavigateBack()
                }
            }, scrollBehavior = scrollBehavior
            )
        },
        content = {
            LazyColumn(modifier = Modifier.padding(it)) {
                if (Build.VERSION.SDK_INT >= 29) item {
                    PreferenceSingleChoiceItem(
                        text = stringResource(R.string.follow_system),
                        selected = themeModel.themeMode == ThemeMode.SYSTEM
                    ) {
                        applicationScope.launch(Dispatchers.IO) {
                            themeModel.themeMode = ThemeMode.entries.toTypedArray()[0]
                            Preferences.edit {
                                putString(
                                    Preferences.themeModeKey, ThemeMode.entries[0].name
                                )
                            }
                        }
                    }
                }
                item {
                    PreferenceSingleChoiceItem(
                        text = stringResource(R.string.on),
                        selected = themeModel.themeMode == ThemeMode.DARK
                    ) {
                        applicationScope.launch(Dispatchers.IO) {
                            themeModel.themeMode = ThemeMode.entries.toTypedArray()[2]
                            Preferences.edit {
                                putString(
                                    Preferences.themeModeKey, ThemeMode.entries[2].name
                                )
                            }
                        }
                    }
                }
                item {
                    PreferenceSingleChoiceItem(
                        text = stringResource(R.string.off),
                        selected = themeModel.themeMode == ThemeMode.LIGHT
                    ) {
                        applicationScope.launch(Dispatchers.IO) {
                            themeModel.themeMode = ThemeMode.entries.toTypedArray()[1]
                            Preferences.edit {
                                putString(
                                    Preferences.themeModeKey, ThemeMode.entries[1].name
                                )
                            }
                        }
                    }
                }
//                item {
//                    PreferenceSubtitle(text = stringResource(R.string.additional_settings))
//                }
//                item {
//                    PreferenceSwitch(title = stringResource(R.string.high_contrast),
//                        icon = Icons.Outlined.Contrast,
//                        isChecked = isHighContrastModeEnabled,
//                        onClick = {
//                            applicationScope.launch(Dispatchers.IO) {
//                                themeModel.themeMode = ThemeMode.entries.toTypedArray()[3]
//                                Preferences.edit {
//                                    putString(
//                                        Preferences.themeModeKey, ThemeMode.entries[3].name
//                                    )
//                                }
//                            }
//                        })
//                }
            }
        })
}