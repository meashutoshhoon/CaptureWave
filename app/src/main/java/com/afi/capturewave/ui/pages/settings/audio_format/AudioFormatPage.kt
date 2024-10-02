package com.afi.capturewave.ui.pages.settings.audio_format

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
import androidx.compose.material.icons.outlined.HighQuality
import androidx.compose.material.icons.rounded.AudioFile
import androidx.compose.material.icons.rounded.Audiotrack
import androidx.compose.material.icons.rounded.BluetoothAudio
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
import com.afi.capturewave.enums.AudioChannels
import com.afi.capturewave.enums.AudioDeviceSource
import com.afi.capturewave.obj.AudioFormat
import com.afi.capturewave.ui.component.BackButton
import com.afi.capturewave.ui.component.CWDialog
import com.afi.capturewave.ui.component.ConfirmButton
import com.afi.capturewave.ui.component.DismissButton
import com.afi.capturewave.ui.component.PreferenceItem
import com.afi.capturewave.ui.component.DialogSingleChoiceItem
import com.afi.capturewave.util.Preferences

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AudioFormatPage(onNavigateBack: () -> Unit) {
    val scrollBehavior =
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState(),
            canScroll = { true })

    var audioSourceDialog by remember { mutableStateOf(false) }
    var audioFormatDialog by remember { mutableStateOf(false) }
    var audioTypeDialog by remember { mutableStateOf(false) }
    var bitrateDialog by remember { mutableStateOf(false) }
    var sampleRateDialog by remember { mutableStateOf(false) }

    Scaffold(modifier = Modifier
        .fillMaxSize()
        .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            LargeTopAppBar(title = {
                Text(
                    modifier = Modifier,
                    text = stringResource(id = R.string.audio_format),
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
                        title = stringResource(id = R.string.audio_format),
                        description = stringResource(id = R.string.audio_format_desc2),
                        icon = Icons.Rounded.AudioFile,
                        onClick = { audioFormatDialog = true },
                    )
                }

                item {
                    PreferenceItem(
                        title = stringResource(id = R.string.sample_rate),
                        description = stringResource(id = R.string.sample_rate_desc),
                        icon = Icons.Outlined.HighQuality,
                        onClick = { sampleRateDialog = true },
                    )
                }

                item {
                    PreferenceItem(
                        title = stringResource(id = R.string.bitrate),
                        description = stringResource(id = R.string.bitrate_desc),
                        icon = Icons.Outlined.HighQuality,
                        onClick = { bitrateDialog = true },
                    )
                }

                item {
                    PreferenceItem(
                        title = stringResource(id = R.string.audio_device),
                        description = stringResource(id = R.string.audio_device_desc2),
                        icon = Icons.Rounded.BluetoothAudio,
                        onClick = { audioSourceDialog = true },
                    )
                }

                item {
                    PreferenceItem(
                        title = stringResource(id = R.string.audio_type),
                        description = stringResource(id = R.string.audio_type_desc),
                        icon = Icons.Rounded.Audiotrack,
                        onClick = { audioTypeDialog = true },
                    )
                }

            }
        })

    if (audioFormatDialog) {
        val values = AudioFormat.formats.map { it.format }
        var audioFormat by remember {
            mutableStateOf(AudioFormat.getCurrent())
        }
        val entries = AudioFormat.formats.map { it.name }
        var selectedOption by remember { mutableIntStateOf(audioFormat.format) }
        CWDialog(onDismissRequest = { audioFormatDialog = false }, dismissButton = {
            DismissButton { audioFormatDialog = false }
        }, icon = { Icon(Icons.Rounded.AudioFile, null) }, title = {
            Text(stringResource(R.string.audio_format))
        }, confirmButton = {
            ConfirmButton {
                audioFormat =
                    AudioFormat.formats.firstOrNull { it.format == selectedOption } ?: audioFormat
                Preferences.edit { putString(Preferences.audioFormatKey, audioFormat.name) }
                audioFormatDialog = false
            }
        }, text = {
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                        .padding(horizontal = 24.dp),
                    text = stringResource(R.string.audio_format_desc2),
                    style = MaterialTheme.typography.bodyLarge
                )
                entries.forEachIndexed { index, entry ->
                    DialogSingleChoiceItem(text = entry,
                        selected = values[index] == selectedOption,
                        onClick = {
                            selectedOption = values[index]
                        },
                    )
                }
            }
        })
    }

    if (bitrateDialog) {
        val pref = Preferences.prefs.getInt(Preferences.audioBitrateKey, -1).takeIf { it != -1 }
        var isAutoEnabled by remember {
            mutableStateOf(pref == null)
        }
        var input by remember {
            mutableStateOf((pref ?: 192_000).toString())
        }
        AlertDialog(onDismissRequest = { bitrateDialog = false },
            title = { Text(text = stringResource(R.string.bitrate)) },
            confirmButton = {
                ConfirmButton {
                    Preferences.edit {
                        putInt(
                            Preferences.audioBitrateKey,
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

    if (sampleRateDialog) {
        val pref = Preferences.prefs.getInt(Preferences.audioSampleRateKey, -1).takeIf { it != -1 }
        var isAutoEnabled by remember {
            mutableStateOf(pref == null)
        }
        var input by remember {
            mutableStateOf((pref ?: 44_100).toString())
        }
        AlertDialog(onDismissRequest = { sampleRateDialog = false },
            title = { Text(text = stringResource(R.string.sample_rate)) },
            confirmButton = {
                ConfirmButton {
                    Preferences.edit {
                        putInt(
                            Preferences.audioSampleRateKey,
                            input.takeIf { !isAutoEnabled }?.toIntOrNull() ?: -1
                        )
                    }
                    sampleRateDialog = false
                }
            },
            dismissButton = {
                DismissButton { sampleRateDialog = false }
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
                        label = { Text(stringResource(R.string.sample_rate)) })
                }
            })
    }

    if (audioSourceDialog) {
        val values = AudioDeviceSource.entries.map { it.value }
        var audioDeviceSource by remember {
            mutableStateOf(
                AudioDeviceSource.fromInt(
                    Preferences.prefs.getInt(
                        Preferences.audioDeviceSourceKey, AudioDeviceSource.DEFAULT.value
                    )
                )
            )
        }
        val entries = listOf(
            R.string.default_audio, R.string.microphone, R.string.camcorder, R.string.unprocessed
        ).map {
            stringResource(it)
        }
        var selectedOption by remember { mutableIntStateOf(audioDeviceSource.value) }
        CWDialog(onDismissRequest = { audioSourceDialog = false }, dismissButton = {
            DismissButton { audioSourceDialog = false }
        }, icon = { Icon(Icons.Rounded.BluetoothAudio, null) }, title = {
            Text(stringResource(R.string.audio_device))
        }, confirmButton = {
            ConfirmButton {
                audioDeviceSource = AudioDeviceSource.fromInt(selectedOption)
                Preferences.edit {
                    putInt(Preferences.audioDeviceSourceKey, selectedOption)
                }
                audioSourceDialog = false
            }
        }, text = {
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                        .padding(horizontal = 24.dp),
                    text = stringResource(R.string.audio_device_desc2),
                    style = MaterialTheme.typography.bodyLarge
                )
                entries.forEachIndexed { index, entry ->
                    DialogSingleChoiceItem(
                        text = entry,
                        selected = values[index] == selectedOption,
                        onClick = {
                            selectedOption = values[index]
                        },
                    )
                }
            }
        })
    }

    if (audioTypeDialog) {
        val values = AudioChannels.entries.map { it.value }
        var audioChannels by remember {
            mutableStateOf(
                AudioChannels.fromInt(
                    Preferences.prefs.getInt(Preferences.audioChannelsKey, AudioChannels.MONO.value)
                )
            )
        }
        val entries = listOf(R.string.mono, R.string.stereo).map {
            stringResource(it)
        }
        var selectedOption by remember { mutableIntStateOf(audioChannels.value) }
        CWDialog(onDismissRequest = { audioTypeDialog = false }, dismissButton = {
            DismissButton { audioTypeDialog = false }
        }, icon = { Icon(Icons.Rounded.Audiotrack, null) }, title = {
            Text(stringResource(R.string.audio_type))
        }, confirmButton = {
            ConfirmButton {
                audioChannels = AudioChannels.fromInt(selectedOption)
                Preferences.edit {
                    putInt(Preferences.audioChannelsKey, selectedOption)
                }
                audioTypeDialog = false
            }
        }, text = {
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                        .padding(horizontal = 24.dp),
                    text = stringResource(R.string.audio_type_desc),
                    style = MaterialTheme.typography.bodyLarge
                )
                entries.forEachIndexed { index, entry ->
                    DialogSingleChoiceItem(
                        text = entry,
                        selected = values[index] == selectedOption,
                        onClick = {
                            selectedOption = values[index]
                        },
                    )
                }
            }
        })
    }

}