package com.afi.capturewave

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import com.afi.capturewave.ui.component.FilledButtonWithIcon
import com.afi.capturewave.ui.component.HorizontalDivider
import com.afi.capturewave.ui.theme.CaptureWaveTheme

class CrashReportActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val errorMessage: String = intent.getStringExtra("error_report").toString()

        setContent {
            CaptureWaveTheme {
                val clipboardManager = LocalClipboardManager.current
                CrashReportPage(errorMessage = errorMessage) {
                    clipboardManager.setText(AnnotatedString(errorMessage))
                    this.finishAffinity()
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
    Scaffold(modifier = Modifier.fillMaxSize(), bottomBar = {
        HorizontalDivider()

        FilledButtonWithIcon(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            onClick = onClick,
            icon = Icons.Outlined.BugReport,
            text = stringResource(R.string.copy_and_exit)
        )
    }) {
        Column(
            modifier = Modifier
                .padding(it)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = stringResource(R.string.unknown_error_title),
                style = MaterialTheme.typography.displaySmall,
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp, top = 60.dp, bottom = 12.dp)
            )
            Text(
                text = errorMessage,
                style = MaterialTheme.typography.bodyMedium.copy(fontFamily = FontFamily.Monospace),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            )
        }
    }
}