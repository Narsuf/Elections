package com.n27.core.data.local.room.mappers

import com.n27.core.data.local.room.models.ResultWithParty
import com.n27.core.domain.models.Result

internal fun ResultWithParty.toResult(electionId: Long) = Result(
    id = result.id,
    partyId = party.partyId,
    electionId = electionId,
    elects = result.elects,
    votes = result.votes,
    party = party.toParty()
)

internal fun Result.toResultWithParty() = ResultWithParty(
    result = toResultRaw(),
    party = party.toPartyRaw()
)
