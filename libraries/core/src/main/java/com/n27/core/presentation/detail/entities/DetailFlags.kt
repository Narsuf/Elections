package com.n27.core.presentation.detail.entities

import com.n27.core.domain.election.Election
import com.n27.core.domain.live.models.LocalElectionIds

data class DetailFlags(
    val election: Election?,
    val isLiveGeneralElection: Boolean,
    val liveRegionalElectionId: String?,
    val liveLocalElectionIds: LocalElectionIds?
)