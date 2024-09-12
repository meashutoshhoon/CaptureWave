package com.afi.capturewave.ui.models

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.documentfile.provider.DocumentFile
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import com.afi.capturewave.App
import com.afi.capturewave.enums.TrimmerState
import com.afi.capturewave.util.MediaTrimmer
import kotlinx.coroutines.launch

class TrimmerModel(context: Context) : ViewModel() {

    @UnstableApi
    val player = ExoPlayer.Builder(context)
        .setUsePlatformDiagnostics(false)
        .build()

    var startTimeStamp by mutableLongStateOf(0L)
    var endTimeStamp by mutableStateOf<Long?>(null)

    var trimmerState: TrimmerState by mutableStateOf(TrimmerState.NoJob)

    @RequiresApi(Build.VERSION_CODES.O)
    fun startTrimmer(context: Context, inputFile: DocumentFile) {
        val endT = endTimeStamp ?: return
        if (endT <= startTimeStamp) return
        viewModelScope.launch {
            trimmerState = TrimmerState.Running
            val trimmer = MediaTrimmer()
            val result = trimmer.trimMedia(context, inputFile, startTimeStamp, endT)
            trimmerState = if (result) {
                TrimmerState.Success
            } else {
                TrimmerState.Failed
            }
        }
    }

    companion object {
        val Factory = viewModelFactory {
            initializer {
                val application =
                    (this[APPLICATION_KEY] as App)
                TrimmerModel(application)
            }
        }
    }
}