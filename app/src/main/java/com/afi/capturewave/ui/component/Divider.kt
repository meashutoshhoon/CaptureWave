package com.afi.capturewave.ui.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DividerDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun HorizontalDivider(
    modifier: Modifier = Modifier,
    color: Color = DividerDefaults.color
) {
    androidx.compose.material3.HorizontalDivider(
        modifier = modifier
            .fillMaxWidth(),
        color = color,
        thickness = DividerDefaults.Thickness
    )
}