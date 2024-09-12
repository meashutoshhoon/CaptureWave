package com.afi.capturewave.ui.models

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import com.afi.capturewave.App
import com.afi.capturewave.enums.SortOrder
import com.afi.capturewave.obj.RecordingItemData
import com.afi.capturewave.util.FileRepository
import kotlinx.coroutines.launch

class PlayerModel(context: Context, private val fileRepository: FileRepository) : ViewModel() {
    @UnstableApi
    var player = ExoPlayer.Builder(context)
        .setUsePlatformDiagnostics(false)
        .build()

    var selectedFiles by mutableStateOf(listOf<RecordingItemData>())

    private var sortOrder = SortOrder.MODIFIED

    var audioRecordingItems by mutableStateOf(listOf<RecordingItemData>())
    var screenRecordingItems by mutableStateOf(listOf<RecordingItemData>())

    init {
        loadFiles()
    }

    fun loadFiles() {
        viewModelScope.launch {
            audioRecordingItems = fileRepository.getAudioRecordingItems(sortOrder)
            screenRecordingItems = fileRepository.getVideoRecordingItems(sortOrder)
        }
    }

    fun sortItems(newSortOrder: SortOrder) {
        if (newSortOrder == sortOrder) return
        sortOrder = newSortOrder
        loadFiles()
    }

    fun deleteFiles() {
        viewModelScope.launch {
            if (selectedFiles.isEmpty()) {
                fileRepository.deleteAllFiles()
                loadFiles()
                return@launch
            }
            fileRepository.deleteFiles(selectedFiles.map { it.recordingFile })
            selectedFiles = emptyList()
            loadFiles()
        }
    }

    fun stopPlaying() {
        player.stop()
    }

    companion object {
        val Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as App)
                PlayerModel(application, application.fileRepository)
            }
        }
    }
}