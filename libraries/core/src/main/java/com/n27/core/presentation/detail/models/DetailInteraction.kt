package com.n27.core.presentation.detail.models

import com.n27.core.domain.election.Election

sealed class DetailInteraction {

    data class ScreenOpened(val flags: DetailFlags) : DetailInteraction()
    data class Refresh(val currentElection: Election?, val flags: DetailFlags) : DetailInteraction()
    data class Swap(
        val senateElection: Election?,
        val currentElection: Election?,
        val flags: DetailFlags
    ) : DetailInteraction()
}