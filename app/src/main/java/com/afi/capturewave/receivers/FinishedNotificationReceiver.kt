package com.afi.capturewave.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationManagerCompat
import com.afi.capturewave.App
import com.afi.capturewave.services.RecorderService
import com.afi.capturewave.util.IntentHelper
import com.afi.capturewave.util.NotificationHelper

class FinishedNotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val fileName = intent.getStringExtra(RecorderService.FILE_NAME_EXTRA_KEY) ?: return
        val file = (context.applicationContext as App).fileRepository
            .getOutputDir().findFile(fileName)

        when (intent.getStringExtra(RecorderService.ACTION_EXTRA_KEY)) {
            RecorderService.SHARE_ACTION -> file?.let { IntentHelper.shareFile(context, it) }
            RecorderService.DELETE_ACTION -> file?.delete()
        }
        NotificationManagerCompat.from(context)
            .cancel(NotificationHelper.RECORDING_FINISHED_N_ID)
    }
}