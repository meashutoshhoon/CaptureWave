package com.afi.capturewave.ui.pages.settings.about

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import com.afi.capturewave.R
import com.afi.capturewave.ui.component.BackButton
import com.afi.capturewave.ui.component.CreditItem
import com.afi.capturewave.ui.items.Credit

const val APACHE_V2 = "Apache License 2.0"
const val BSD = "BSD 3-Clause \"New\" or \"Revised\" License"
const val GPL_V3 = "GNU General Public License v3.0"

const val AOSP = "The Android Open Source Project"
const val JETBRAINS = "JetBrains Team"
const val TENCENT = "Tencent Wechat, Inc."
const val SQUARE = "Square, Inc."
const val YOU_APPS = "You Apps"


const val recordYou = "https://github.com/you-apps/RecordYou"


const val navigation = "https://developer.android.com/develop/ui/compose/navigation"
const val bom = "https://github.com/chrisbanes/compose-bom"
const val splash = "https://developer.android.com/develop/ui/views/launch/splash-screen"
const val materialIcon = "https://fonts.google.com/icons"
const val jetpack = "https://github.com/androidx/androidx"
const val mmkv = "https://github.com/Tencent/MMKV"
const val kotlin = "https://kotlinlang.org/"
const val okhttp = "https://github.com/square/okhttp"
const val material3 = "https://m3.material.io/"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreditsPage(onNavigateBack: () -> Unit) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(
        rememberTopAppBarState(),
        canScroll = { true }
    )

    val creditsList = listOf(
        Credit("Android Jetpack", AOSP, APACHE_V2, "", jetpack),
        Credit("Compose Navigation", AOSP, APACHE_V2, "2.8.0", navigation),
        Credit("Kotlin", JETBRAINS, APACHE_V2, "2.0.0", kotlin),
        Credit("MMKV", TENCENT, BSD, "1.3.2", mmkv),
        Credit("okhttp", SQUARE, APACHE_V2, "5.0.0-alpha.10", okhttp),
        Credit("Snapper for Jetpack Compose", "Chris Banes", APACHE_V2, "2024.08.00-alpha01", bom),
        Credit("SplashScreen", AOSP, APACHE_V2, "1.0.1", splash),
        Credit("Material Design 3", AOSP, APACHE_V2, "1.12.0", material3),
        Credit("Material Icons", AOSP, APACHE_V2, "", materialIcon),
        Credit("RecordYou", YOU_APPS, GPL_V3, "", recordYou)
    )

    val uriHandler = LocalUriHandler.current
    fun openUrl(url: String) {
        uriHandler.openUri(url)
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            LargeTopAppBar(
                title = {
                    Text(
                        modifier = Modifier,
                        text = stringResource(id = R.string.credits),
                    )
                }, navigationIcon = {
                    BackButton {
                        onNavigateBack()
                    }
                }, scrollBehavior = scrollBehavior
            )
        }, content = {
            LazyColumn(modifier = Modifier.padding(it)) {
                items(creditsList) { item ->
                    CreditItem(
                        credit = item,
                    ) {
                        openUrl(item.url)
                    }
                }
            }
        }
    )
}