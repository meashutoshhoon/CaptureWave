package com.afi.capturewave.ui.pages.settings.recorder

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.BluetoothAudio
import androidx.compose.material.icons.rounded.VideoFile
import androidx.compose.material.icons.rounded.VideoSettings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.afi.capturewave.R
import com.afi.capturewave.enums.AudioSource
import com.afi.capturewave.enums.VideoFormat
import com.afi.capturewave.ui.component.BackButton
import com.afi.capturewave.ui.component.CWDialog
import com.afi.capturewave.ui.component.ConfirmButton
import com.afi.capturewave.ui.component.DismissButton
import com.afi.capturewave.ui.component.PreferenceItem
import com.afi.capturewave.ui.component.SingleChoiceItem
import com.afi.capturewave.util.Preferences

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenRecorderPage(onNavigateBack: () -> Unit) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(
        rememberTopAppBarState(),
        canScroll = { true })

    var audioSourceDialog by remember { mutableStateOf(false) }
    var videoFormatDialog by remember { mutableStateOf(false) }
    var bitrateDialog by remember { mutableStateOf(false) }

    Scaffold(modifier = Modifier
        .fillMaxSize()
        .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            LargeTopAppBar(title = {
                Text(
                    modifier = Modifier,
                    text = stringResource(id = R.string.screen_recorder),
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
                item {
                    PreferenceItem(
                        title = stringResource(id = R.string.audio_device),
                        description = stringResource(id = R.string.audio_device_desc),
                        icon = Icons.Rounded.BluetoothAudio,
                        onClick = { audioSourceDialog = true },
                    )
                }

                item {
                    PreferenceItem(
                        title = stringResource(id = R.string.video_format),
                        description = stringResource(id = R.string.video_format_desc),
                        icon = Icons.Rounded.VideoFile,
                        onClick = { videoFormatDialog = true },
                    )
                }

                item {
                    PreferenceItem(
                        title = stringResource(id = R.string.bitrate),
                        description = stringResource(id = R.string.bitrate_desc),
                        icon = Icons.Rounded.VideoSettings,
                        onClick = { bitrateDialog = true },
                    )
                }
            }
        })

    if (audioSourceDialog) {
        val audioValues = AudioSource.entries.map { it.value }
        var screenAudioSource by remember {
            mutableStateOf(
                AudioSource.fromInt(Preferences.prefs.getInt(Preferences.audioSourceKey, 0))
            )
        }
        val entries = listOf(R.string.no_audio, R.string.microphone).map {
            stringResource(it)
        }
        var selectedOption by remember { mutableIntStateOf(screenAudioSource.value) }
        CWDialog(onDismissRequest = { audioSourceDialog = false }, dismissButton = {
            DismissButton { audioSourceDialog = false }
        }, icon = { Icon(Icons.Rounded.BluetoothAudio, null) }, title = {
            Text(stringResource(R.string.audio_device))
        }, confirmButton = {
            ConfirmButton {
                screenAudioSource = AudioSource.fromInt(selectedOption)
                Preferences.edit { putInt(Preferences.audioSourceKey, selectedOption) }
                audioSourceDialog = false
            }
        }, text = {
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                        .padding(horizontal = 24.dp),
                    text = stringResource(R.string.audio_device_desc),
                    style = MaterialTheme.typography.bodyLarge
                )
                entries.forEachIndexed { index, entry ->
                    SingleChoiceItem(
                        text = entry,
                        selected = audioValues[index] == selectedOption,
                        onClick = {
                            selectedOption = audioValues[index]
                        })
                }
            }
        })
    }

    if (videoFormatDialog) {
        val values = VideoFormat.codecs.map { it.codec }
        var videoEncoder by remember {
            mutableStateOf(VideoFormat.getCurrent())
        }
        val entries = VideoFormat.codecs.map { it.name }
        var selectedOption by remember { mutableIntStateOf(videoEncoder.codec) }
        CWDialog(onDismissRequest = { videoFormatDialog = false }, dismissButton = {
            DismissButton { videoFormatDialog = false }
        }, icon = { Icon(Icons.Rounded.VideoFile, null) }, title = {
            Text(stringResource(R.string.video_format))
        }, confirmButton = {
            ConfirmButton {
                videoEncoder =
                    VideoFormat.codecs.firstOrNull { it.codec == selectedOption } ?: videoEncoder
                Preferences.edit { putInt(Preferences.videoCodecKey, selectedOption) }
                videoFormatDialog = false
            }
        }, text = {
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                        .padding(horizontal = 24.dp),
                    text = stringResource(R.string.video_format_desc),
                    style = MaterialTheme.typography.bodyLarge
                )
                entries.forEachIndexed { index, entry ->
                    SingleChoiceItem(
                        text = entry,
                        selected = values[index] == selectedOption,
                        onClick = {
                            selectedOption = values[index]
                        })
                }
            }
        })
    }

    if (bitrateDialog) {
        val pref = Preferences.prefs.getInt(Preferences.videoBitrateKey, -1).takeIf { it != -1 }
        var isAutoEnabled by remember {
            mutableStateOf(pref == null)
        }
        var input by remember {
            mutableStateOf((pref ?: 1_200_000).toString())
        }
        AlertDialog(onDismissRequest = { bitrateDialog = false },
            title = { Text(text = stringResource(R.string.bitrate)) },
            confirmButton = {
                ConfirmButton {
                    Preferences.edit {
                        putInt(
                            Preferences.videoBitrateKey,
                            input.takeIf { !isAutoEnabled }?.toIntOrNull() ?: -1
                        )
                    }
                    bitrateDialog = false
                }
            },
            dismissButton = {
                DismissButton { bitrateDialog = false }
            },
            text = {
                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(checked = isAutoEnabled, onCheckedChange = { isAutoEnabled = it })
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(text = stringResource(R.string.auto))
                    }
                    Spacer(modifier = Modifier.height(5.dp))
                    OutlinedTextField(modifier = Modifier.padding(horizontal = 5.dp),
                        value = input,
                        onValueChange = { input = it },
                        enabled = !isAutoEnabled,
                        shape = RoundedCornerShape(13.dp),
                        readOnly = isAutoEnabled,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        label = { Text(stringResource(R.string.bitrate)) })
                }
            })
    }
}