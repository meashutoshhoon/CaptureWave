package com.afi.capturewave.obj

import android.graphics.Bitmap
import androidx.documentfile.provider.DocumentFile
import com.afi.capturewave.enums.RecorderType

data class RecordingItemData(
    val recordingFile: DocumentFile,
    val recorderType: RecorderType,
    val thumbnail: Bitmap? = null
) {
    val isAudio get() = recorderType == RecorderType.AUDIO
    val isVideo get() = recorderType == RecorderType.VIDEO
}