package com.n27.core.data.local.room.mappers

import com.n27.core.data.models.Result
import com.n27.core.data.local.room.models.ResultWithParty

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
