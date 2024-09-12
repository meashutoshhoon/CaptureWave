package com.afi.capturewave.util

import android.widget.Toast
import com.afi.capturewave.App.Companion.applicationScope
import com.afi.capturewave.App.Companion.context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object ToastUtil {
    fun makeToast(text: String) {
        Toast.makeText(context.applicationContext, text, Toast.LENGTH_SHORT).show()
    }

    fun makeToastSuspend(text: String) {
        applicationScope.launch(Dispatchers.Main) {
            makeToast(text)
        }
    }

    fun makeToast(stringId: Int) {
        Toast.makeText(context.applicationContext, context.getString(stringId), Toast.LENGTH_SHORT)
            .show()
    }
}