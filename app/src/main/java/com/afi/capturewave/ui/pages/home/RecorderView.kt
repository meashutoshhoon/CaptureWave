package com.afi.capturewave.ui.pages.home

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.afi.capturewave.enums.RecorderState
import com.afi.capturewave.ui.models.RecorderModel

@Composable
fun RecorderView(
    recordScreenMode: Boolean
) {
    val recorderModel: RecorderModel = viewModel(LocalContext.current as ComponentActivity)

    LaunchedEffect(recorderModel.recorderState) {
        if (recorderModel.recorderState == RecorderState.IDLE) {
            recorderModel.stopRecording()
        }
    }

    Scaffold {
        ResponsiveRecordScreenLayout(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            PaneOne = {
                RecorderPreview(recordScreenMode)
            },
            PaneTwo = {
                RecorderController(recordScreenMode)
            }
        )
    }
}