package com.n27.core.data.local.room.mappers

import com.n27.core.data.local.room.models.ElectionRaw
import com.n27.core.domain.election.models.Election

internal fun Election.toElectionRaw() = ElectionRaw(
    electionId = id,
    name = name,
    date = date,
    place = place,
    chamberName = chamberName,
    totalElects = totalElects,
    scrutinized = scrutinized,
    validVotes = validVotes,
    abstentions = abstentions,
    blankVotes = blankVotes,
    nullVotes = nullVotes
)
