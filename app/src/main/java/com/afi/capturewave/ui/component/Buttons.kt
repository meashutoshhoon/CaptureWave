package com.afi.capturewave.ui.component

import android.view.SoundEffectConstants
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.afi.capturewave.R

@Composable
fun ConfirmButton(
    text: String = stringResource(R.string.confirm), enabled: Boolean = true, onClick: () -> Unit
) {
    TextButton(onClick = onClick, enabled = enabled) {
        Text(text)
    }
}

@Composable
fun DismissButton(text: String = stringResource(R.string.dismiss), onClick: () -> Unit) {
    TextButton(onClick = onClick) {
        Text(text)
    }
}

@Composable
fun FilledButtonWithIcon(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    text: String,
    enabled: Boolean = true,
    onClick: () -> Unit,
) {
    Button(
        modifier = modifier,
        onClick = onClick,
        contentPadding = ButtonDefaults.ButtonWithIconContentPadding,
        enabled = enabled
    ) {
        Icon(
            modifier = Modifier.size(18.dp), imageVector = icon, contentDescription = null
        )
        Text(
            modifier = Modifier.padding(start = 6.dp), text = text
        )
    }
}

@Composable
fun DialogButton(
    text: String,
    onClick: () -> Unit
) {
    val view = LocalView.current
    TextButton(onClick = {
        view.playSoundEffect(SoundEffectConstants.CLICK)
        onClick.invoke()
    }) {
        Text(text)
    }
}