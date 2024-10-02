package com.afi.capturewave.util

import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.core.os.LocaleListCompat
import com.afi.capturewave.R
import java.util.Locale

// Do not modify
private const val ENGLISH = 2
private const val HINDI = 28

val LocaleLanguageCodeMap =
    mapOf(
        Locale("en", "US") to ENGLISH,
        Locale("hi") to HINDI,
    )

@Composable
fun Locale?.toDisplayName(): String =
    this?.getDisplayName(this) ?: stringResource(id = R.string.follow_system)

fun setLanguage(locale: Locale?) {
    val localeList =
        locale?.let { LocaleListCompat.create(it) } ?: LocaleListCompat.getEmptyLocaleList()
    AppCompatDelegate.setApplicationLocales(localeList)
}