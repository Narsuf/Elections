package com.n27.core.data.room.mappers

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.GenericTypeIndicator
import com.n27.core.data.models.Election
import com.n27.core.data.models.Party
import com.n27.core.data.models.Result
import com.n27.core.data.room.models.ElectionRaw
import com.n27.core.data.room.models.ElectionWithResultsAndParty
import com.n27.core.data.room.models.PartyRaw
import com.n27.core.data.room.models.ResultRaw
import com.n27.core.data.room.models.ResultWithParty

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
