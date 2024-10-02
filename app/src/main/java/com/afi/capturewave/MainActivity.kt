package com.afi.capturewave

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.media.projection.MediaProjectionManager
import android.os.Build
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.afi.capturewave.App.Companion.context
import com.afi.capturewave.enums.RecorderType
import com.afi.capturewave.ui.common.LocalDarkTheme
import com.afi.capturewave.ui.common.SettingsProvider
import com.afi.capturewave.ui.models.RecorderModel
import com.afi.capturewave.ui.pages.AppEntry
import com.afi.capturewave.ui.theme.CaptureWaveTheme
import com.afi.capturewave.util.PreferenceUtil
import com.afi.capturewave.util.setLanguage
import kotlinx.coroutines.runBlocking

class MainActivity : AppCompatActivity() {
    private var initialRecorder = RecorderType.NONE
    private var exitAfterRecordingStart = false
    private val recorderModel: RecorderModel by viewModels()
    private lateinit var mProjectionManager: MediaProjectionManager
    private lateinit var launcher: ActivityResultLauncher<Intent>

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT < 33) {
            runBlocking { setLanguage(PreferenceUtil.getLocaleFromPreference()) }
        }
        enableEdgeToEdge()

        context = this.baseContext

        launcher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    recorderModel.startVideoRecorder(this, result)
                }
            }

        mProjectionManager = getSystemService(Context.MEDIA_PROJECTION_SERVICE) as MediaProjectionManager
        processIntent(intent)

        setContent {
            val windowSizeClass = calculateWindowSizeClass(this)
            SettingsProvider(windowWidthSizeClass = windowSizeClass.widthSizeClass) {
                CaptureWaveTheme(
                    darkTheme = LocalDarkTheme.current.isDarkTheme(),
                    isHighContrastModeEnabled = LocalDarkTheme.current.isHighContrastModeEnabled,
                ) {
                    AppEntry(initialRecorder = initialRecorder)
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        processIntent(intent)
        super.onNewIntent(intent)
    }

    private fun processIntent(intent: Intent) {
        val initialRecorderType = intent.getStringExtra(EXTRA_ACTION_KEY)?.let {
            RecorderType.valueOf(it)
        } ?: RecorderType.NONE
        initialRecorder = initialRecorderType
        if (initialRecorderType == RecorderType.AUDIO) {
            recorderModel.startAudioRecorder(this)
        } else if (initialRecorderType == RecorderType.VIDEO) {
            if (recorderModel.hasScreenRecordingPermissions(this)) {
                launcher.launch(mProjectionManager.createScreenCaptureIntent())
            }
        }
        intent.removeExtra(EXTRA_ACTION_KEY)
    }

    override fun onPause() {
        super.onPause()
        if (initialRecorder == RecorderType.VIDEO) {
            exitAfterRecordingStart = true
            initialRecorder = RecorderType.NONE
        }
    }

    override fun onResume() {
        super.onResume()
        if (exitAfterRecordingStart) {
            exitAfterRecordingStart = false
            moveTaskToBack(true)
        }
    }

    companion object {
        const val EXTRA_ACTION_KEY = "action"
    }
}