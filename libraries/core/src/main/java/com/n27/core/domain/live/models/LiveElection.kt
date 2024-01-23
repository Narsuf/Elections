package com.n27.core.domain.live.models

import com.n27.core.domain.election.models.Election

data class LiveElection(
    val id: String?,
    val election: Election
)