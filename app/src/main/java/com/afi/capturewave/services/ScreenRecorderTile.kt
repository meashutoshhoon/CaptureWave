package com.afi.capturewave.services

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.service.quicksettings.TileService
import androidx.annotation.RequiresApi
import com.afi.capturewave.enums.RecorderType
import com.afi.capturewave.MainActivity

@RequiresApi(Build.VERSION_CODES.N)
class ScreenRecorderTile : TileService() {
    @SuppressLint("StartActivityAndCollapseDeprecated")
    override fun onClick() {
        super.onClick()
        val intent = Intent(this, MainActivity::class.java)
            .putExtra(MainActivity.EXTRA_ACTION_KEY, RecorderType.VIDEO.name)
            .apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            startActivityAndCollapse(
                PendingIntent.getActivity(
                    this, PENDING_INTENT_REQUEST_CODE, intent, PendingIntent.FLAG_IMMUTABLE
                )
            )
        } else {
            startActivityAndCollapse(intent)
        }
    }

    companion object {
        const val PENDING_INTENT_REQUEST_CODE = 21
    }
}