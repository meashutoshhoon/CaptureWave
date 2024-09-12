package com.afi.capturewave.enums

sealed interface TrimmerState {
    data object NoJob : TrimmerState
    data object Running : TrimmerState
    data object Success : TrimmerState
    data object Failed : TrimmerState
}