package com.n27.core.presentation.detail.entities

import com.n27.core.data.models.Election

sealed class DetailState {

    object InitialLoading : DetailState()
    object Loading : DetailState()
    data class Content(val election: Election) : DetailState()
    data class Error(val error: String? = null) : DetailState()
}
