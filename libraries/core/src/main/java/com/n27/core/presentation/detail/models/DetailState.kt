package com.n27.core.presentation.detail.models

import com.n27.core.data.models.Election

sealed class DetailState {

    object Loading : DetailState()
    object Content : DetailState()
    data class Error(val error: String? = null) : DetailState()
}
