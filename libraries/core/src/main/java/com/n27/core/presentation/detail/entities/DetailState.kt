package com.n27.core.presentation.detail.entities

import com.n27.core.domain.election.models.Election

sealed class DetailState {

    object Loading : DetailState()
    data class Error(val error: String? = null) : DetailState()
    data class Content(val election: Election) : DetailState()
}
