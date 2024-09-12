package com.afi.capturewave.enums

import com.afi.capturewave.util.Preferences


enum class ThemeMode {
    SYSTEM,
    LIGHT,
    DARK,
    AMOLED
    ;

    companion object {
        fun getCurrent() = valueOf(Preferences.getString(Preferences.themeModeKey, SYSTEM.name))
    }
}