package com.afi.capturewave.enums

enum class AudioSource(val value: Int) {
    NONE(0),
    MICROPHONE(1);

    companion object {
        fun fromInt(value: Int) = entries.first { it.value == value }
    }
}