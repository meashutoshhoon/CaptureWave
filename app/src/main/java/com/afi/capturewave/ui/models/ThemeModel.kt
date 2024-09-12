package com.afi.capturewave.ui.models

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.afi.capturewave.enums.ThemeMode

class ThemeModel : ViewModel() {
    var themeMode by mutableStateOf(
        ThemeMode.getCurrent()
    )
}