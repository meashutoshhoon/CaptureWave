package com.afi.capturewave.enums

enum class AudioChannels(val value: Int) {
    MONO(1),
    STEREO(2);

    companion object {
        fun fromInt(value: Int) = entries.first { it.value == value }
    }
}