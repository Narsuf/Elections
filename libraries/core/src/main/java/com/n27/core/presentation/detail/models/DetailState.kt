package com.n27.core.presentation.detail.models

sealed class DetailState {

    object Content : DetailState()

    data class Loading(val isAnimation: Boolean = true) : DetailState()

    data class Error(val error: String? = null) : DetailState()
}
