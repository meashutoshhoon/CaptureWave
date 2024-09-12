package com.afi.capturewave.util

import com.afi.capturewave.App
import com.afi.capturewave.App.Companion.isFDroidBuild
import com.tencent.mmkv.MMKV

const val AUTO_UPDATE = "auto_update"
const val UPDATE_CHANNEL = "update_channel"
const val CELLULAR_DOWNLOAD = "cellular_download"

const val STABLE = 0
const val PRE_RELEASE = 1

const val TARGET_FOLDER_KEY = "targetFolderKey"

private val BooleanPreferenceDefaults = mapOf(
    CELLULAR_DOWNLOAD to false,
)

private val IntPreferenceDefaults = mapOf(
    UPDATE_CHANNEL to STABLE,
)

private val StringPreferenceDefaults = mapOf(
    TARGET_FOLDER_KEY to "default",
)


private val kv: MMKV = MMKV.defaultMMKV()

object PreferenceUtil {
    fun String.getInt(default: Int = IntPreferenceDefaults.getOrElse(this) { 0 }): Int =
        kv.decodeInt(this, default)

    fun String.getString(default: String = StringPreferenceDefaults.getOrElse(this) { "" }): String =
        kv.decodeString(this) ?: default

    fun String.getBoolean(default: Boolean = BooleanPreferenceDefaults.getOrElse(this) { false }): Boolean =
        kv.decodeBool(this, default)

    fun String.getLong(default: Long) =
        kv.decodeLong(this, default)

    fun String.updateString(newString: String) = kv.encode(this, newString)

    fun String.updateInt(newInt: Int) = kv.encode(this, newInt)

    fun String.updateLong(newLong: Long) = kv.encode(this, newLong)

    fun String.updateBoolean(newValue: Boolean) = kv.encode(this, newValue)

    fun updateValue(key: String, b: Boolean) = key.updateBoolean(b)

    fun encodeInt(key: String, int: Int) = key.updateInt(int)

    fun encodeString(key: String, string: String) = key.updateString(string)

    fun containsKey(key: String) = kv.containsKey(key)

    fun isAutoUpdateEnabled() = AUTO_UPDATE.getBoolean(!isFDroidBuild())
    fun isNetworkAvailableForDownload() =
        CELLULAR_DOWNLOAD.getBoolean() || !App.connectivityManager.isActiveNetworkMetered
}