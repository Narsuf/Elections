package com.n27.core.data.room.mappers

import com.n27.core.data.models.Result
import com.n27.core.data.room.models.ResultRaw

internal fun Result.toResultRaw() = ResultRaw(
    id = id,
    resultPartyId = partyId,
    resultElectionId = electionId,
    elects = elects,
    votes = votes
)
