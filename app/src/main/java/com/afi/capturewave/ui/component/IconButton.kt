package com.afi.capturewave.ui.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import com.afi.capturewave.R
import com.afi.capturewave.ui.common.HapticFeedback.slightHapticFeedback

@Composable
fun BackButton(onClick: () -> Unit) {
    val view = LocalView.current
    IconButton(modifier = Modifier, onClick = {
        onClick()
        view.slightHapticFeedback()
    }) {
        Icon(
            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
            contentDescription = stringResource(R.string.back),
        )
    }
}

@Composable
fun ClickableIcon(
    modifier: Modifier = Modifier,
    imageVector: ImageVector,
    contentDescription: String? = null,
    onClick: () -> Unit
) {
    val view = LocalView.current
    IconButton(modifier = modifier, onClick = {
        onClick()
        view.slightHapticFeedback()
    }) {
        Icon(imageVector, contentDescription)
    }
}