package com.afi.capturewave.canvas

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class CanvasViewModel : ViewModel() {
    var currentPath by mutableStateOf(PathProperties())
    val paths = mutableStateListOf<PathProperties>()
}