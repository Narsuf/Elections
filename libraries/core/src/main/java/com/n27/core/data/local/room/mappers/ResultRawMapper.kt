package com.n27.core.data.local.room.mappers

import com.n27.core.data.local.room.models.ResultRaw
import com.n27.core.domain.election.Result

internal fun Result.toResultRaw() = ResultRaw(
    id = id,
    resultPartyId = partyId,
    resultElectionId = electionId,
    elects = elects,
    votes = votes
)
