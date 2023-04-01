package com.n27.core.presentation.detail

import com.n27.core.data.models.Election

sealed class DetailState {

    object InitialLoading : DetailState()
    object Loading : DetailState()
    data class Success(val election: Election) : DetailState()
    data class Failure(val error: String? = null) : DetailState()
}
