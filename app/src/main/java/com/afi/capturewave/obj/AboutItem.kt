package com.afi.capturewave.obj

import androidx.compose.ui.graphics.vector.ImageVector

data class AboutItem(
    val title: Int,
    val icon: ImageVector,
    val url: String? = null
)