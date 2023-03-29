package com.n27.core.data.room.mappers

import com.n27.core.data.models.Election
import com.n27.core.data.room.models.ElectionRaw

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
