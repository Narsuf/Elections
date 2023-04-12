package com.n27.core.presentation.detail.models

import com.n27.core.data.models.Election

sealed class DetailState {

    object Content : DetailState()

    data class Loading(val isAnimation: Boolean = true) : DetailState()

    data class Error(val error: String? = null) : DetailState()
}
