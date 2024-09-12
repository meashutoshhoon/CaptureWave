package com.afi.capturewave.ui.pages.settings.general

import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Sort
import androidx.compose.material.icons.outlined.Folder
import androidx.compose.material.icons.rounded.Audiotrack
import androidx.compose.material.icons.rounded.Equalizer
import androidx.compose.material.icons.rounded.VideoSettings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.afi.capturewave.R
import com.afi.capturewave.ui.component.BackButton
import com.afi.capturewave.ui.component.ConfirmButton
import com.afi.capturewave.ui.component.DismissButton
import com.afi.capturewave.ui.component.PreferenceItem
import com.afi.capturewave.ui.component.PreferenceSwitch
import com.afi.capturewave.util.FileRepositoryImpl
import com.afi.capturewave.util.PickFolderContract
import com.afi.capturewave.util.Preferences
import com.afi.capturewave.util.TARGET_FOLDER_KEY
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(
    ExperimentalMaterial3Api::class
)
@Composable
fun GeneralPage(
    onNavigateBack: () -> Unit,
) {
    val scope = rememberCoroutineScope()

    val scrollBehavior =
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState(),
            canScroll = { true })

    var namingPatternDialog by remember { mutableStateOf(false) }

    val directoryPicker = rememberLauncherForActivityResult(PickFolderContract()) {
        it ?: return@rememberLauncherForActivityResult
        Preferences.edit { putString(Preferences.targetFolderKey, it.toString()) }
    }

    val directoryPickerFunction = {
        val lastDir = Preferences.prefs.getString(Preferences.targetFolderKey, "")
            .takeIf { !it.isNullOrBlank() }
        directoryPicker.launch(lastDir?.let { Uri.parse(it) })
    }

    Scaffold(modifier = Modifier
        .fillMaxSize()
        .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            LargeTopAppBar(title = {
                Text(
                    text = stringResource(id = R.string.general_settings),
                )
            }, navigationIcon = {
                BackButton { onNavigateBack() }
            }, scrollBehavior = scrollBehavior
            )
        },
        content = {
            LazyColumn(
                modifier = Modifier.padding(it)
            ) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    item {
                        var checked by remember {
                            mutableStateOf(
                                Preferences.prefs.getBoolean(Preferences.losslessRecorderKey, false)
                            )
                        }
                        PreferenceSwitch(
                            title = stringResource(R.string.lossless_audio),
                            description = stringResource(R.string.lossless_audio_desc),
                            icon = Icons.Rounded.Audiotrack,
                            onClick = {
                                checked = !checked
                                Preferences.edit {
                                    putBoolean(
                                        Preferences.losslessRecorderKey, checked
                                    )
                                }
                            },
                            isChecked = checked
                        )
                    }
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    item {
                        var checked by remember {
                            mutableStateOf(
                                Preferences.prefs.getBoolean(
                                    Preferences.showOverlayAnnotationToolKey, false
                                )
                            )
                        }
                        PreferenceSwitch(
                            title = stringResource(R.string.screen_recorder_annotation),
                            description = stringResource(R.string.screen_recorder_annotation_desc),
                            icon = Icons.Rounded.VideoSettings,
                            onClick = {
                                checked = !checked
                                Preferences.edit {
                                    putBoolean(
                                        Preferences.showOverlayAnnotationToolKey, checked
                                    )
                                }
                            },
                            isChecked = checked
                        )
                    }
                }
                item {
                    var checked by remember {
                        mutableStateOf(
                            Preferences.prefs.getBoolean(
                                Preferences.showVisualizerTimestamps, false
                            )
                        )
                    }
                    PreferenceSwitch(
                        title = stringResource(R.string.audio_visualizer_timestamps),
                        description = stringResource(R.string.audio_visualizer_timestamps_description),
                        icon = Icons.Rounded.Equalizer,
                        onClick = {
                            checked = !checked
                            Preferences.edit {
                                putBoolean(
                                    Preferences.showVisualizerTimestamps, checked
                                )
                            }
                        },
                        isChecked = checked
                    )
                }

                item {
                    PreferenceItem(title = stringResource(R.string.naming_pattern),
                        description = stringResource(id = R.string.naming_pattern_desc),
                        icon = Icons.AutoMirrored.Rounded.Sort,
                        onClick = { namingPatternDialog = true })
                }

                item {
                    PreferenceItem(title = stringResource(R.string.download_directory),
                        description = stringResource(id = R.string.download_directory_desc),
                        icon = Icons.Outlined.Folder,
                        onClick = {
                            directoryPickerFunction()
                        })
                }
            }
        })

    if (namingPatternDialog) {
        var value by remember {
            mutableStateOf(
                Preferences.getString(
                    Preferences.namingPatternKey, FileRepositoryImpl.DEFAULT_NAMING_PATTERN
                )
            )
        }

        AlertDialog(onDismissRequest = { namingPatternDialog = false },
            icon = { Icon(Icons.AutoMirrored.Rounded.Sort, null) },
            title = { Text(stringResource(id = R.string.naming_pattern)) },
            dismissButton = {
                DismissButton { namingPatternDialog = false }
            },
            text = {
                Column(modifier = Modifier) {
                    Text(
                        stringResource(
                            R.string.naming_pattern_dialog_desc
                        ), style = MaterialTheme.typography.bodyLarge
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(value = value,
                        onValueChange = { value = it },
                        shape = RoundedCornerShape(13.dp),
                        label = { Text(stringResource(R.string.naming_pattern)) })
                }

            },
            confirmButton = {
                ConfirmButton {
                    namingPatternDialog = false
                    scope.launch(Dispatchers.IO) {
                        Preferences.edit { putString(Preferences.namingPatternKey, value) }
                    }
                }
            })
    }
}