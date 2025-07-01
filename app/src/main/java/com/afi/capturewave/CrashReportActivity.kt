package com.afi.capturewave

import android.content.ClipData
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BugReport
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import com.afi.capturewave.ui.common.LocalDarkTheme
import com.afi.capturewave.ui.common.SettingsProvider
import com.afi.capturewave.ui.component.FilledButtonWithIcon
import com.afi.capturewave.ui.theme.CaptureWaveTheme
import kotlinx.coroutines.launch

class CrashReportActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val errorMessage: String = intent.getStringExtra("error_report").toString()

        setContent {
            SettingsProvider(WindowWidthSizeClass.Compact) {
                CaptureWaveTheme(
                    darkTheme = LocalDarkTheme.current.isDarkTheme(),
                    isHighContrastModeEnabled = LocalDarkTheme.current.isHighContrastModeEnabled,
                ) {
                    val clipboard = LocalClipboard.current
                    val scope = rememberCoroutineScope()
                    CrashReportPage(errorMessage = errorMessage) {
                        scope.launch {
                            val clip = ClipData.newPlainText("label", AnnotatedString(errorMessage))
                            clipboard.nativeClipboard.setPrimaryClip(clip)
                        }
                        this.finishAffinity()
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (isFinishing) finishAffinity()
    }
}

@Composable
fun CrashReportPage(errorMessage: String = "ERROR_EXAMPLE", onClick: () -> Unit = {}) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            HorizontalDivider()

            FilledButtonWithIcon(
                modifier =
                Modifier.fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                onClick = onClick,
                icon = Icons.Outlined.BugReport,
                text = stringResource(R.string.copy_and_exit),
            )
        },
    ) {
        Column(modifier = Modifier.padding(it).verticalScroll(rememberScrollState())) {
            Text(
                text = stringResource(R.string.unknown_error_title),
                style = MaterialTheme.typography.displaySmall,
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 60.dp, bottom = 12.dp),
            )
            Text(
                text = errorMessage,
                style = MaterialTheme.typography.bodyMedium.copy(fontFamily = FontFamily.Monospace),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(16.dp).fillMaxWidth(),
            )
        }
    }
}