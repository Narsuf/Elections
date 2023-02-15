package com.jorgedguezm.elections.data

import com.jorgedguezm.elections.data.models.Election
import com.jorgedguezm.elections.data.models.Party
import com.jorgedguezm.elections.data.models.Result
import com.jorgedguezm.elections.data.room.*
import com.jorgedguezm.elections.data.room.models.*

internal fun ElectionWithResultsAndParty.toElection() = Election(
    id = election.id,
    name = election.name,
    date = election.date,
    place = election.place,
    chamberName = election.chamberName,
    totalElects = election.totalElects,
    scrutinized = election.scrutinized,
    validVotes = election.validVotes,
    abstentions = election.abstentions,
    blankVotes = election.blankVotes,
    nullVotes = election.nullVotes,
    results = results.map { it.toResult(election.id) }

)

private fun ResultWithParty.toResult(electionId: Long) = Result(
    id = result.id,
    partyId = party.id,
    electionId = electionId,
    elects = result.elects,
    votes = result.votes,
    party = party.toParty()
)

private fun PartyRaw.toParty() = Party(
    id = id,
    name = name,
    color = color
)

internal fun Election.toElectionWithResultsAndParty() = ElectionWithResultsAndParty(
    election = toElectionRaw(),
    results = results.map { it.toResultWithParty() }
)

private fun Election.toElectionRaw() = ElectionRaw(
    id = id,
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

private fun Result.toResultWithParty() = ResultWithParty(
    result = toResultRaw(),
    party = party.toPartyRaw()
)

private fun Result.toResultRaw() = ResultRaw(
    id = id,
    partyId = partyId,
    electionId = electionId,
    elects = elects,
    votes = votes
)

private fun Party.toPartyRaw() = PartyRaw(
    id = id,
    name = name,
    color = color
)