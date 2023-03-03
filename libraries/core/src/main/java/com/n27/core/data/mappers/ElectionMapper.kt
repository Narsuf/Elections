package com.n27.core.data.mappers

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.GenericTypeIndicator
import com.n27.core.data.models.Election
import com.n27.core.data.models.Party
import com.n27.core.data.models.Result
import com.n27.core.data.room.ElectionRaw
import com.n27.core.data.room.ElectionWithResultsAndParty
import com.n27.core.data.room.PartyRaw
import com.n27.core.data.room.ResultRaw
import com.n27.core.data.room.ResultWithParty

fun DataSnapshot.toElections() = getValue(object : GenericTypeIndicator<List<Election>>() { })

fun List<ElectionWithResultsAndParty>.toElections() = map { it.toElection() }

internal fun ElectionWithResultsAndParty.toElection() = Election(
    id = election.electionId,
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
    results = results.map { it.toResult(election.electionId) }

)

private fun ResultWithParty.toResult(electionId: Long) = Result(
    id = result.id,
    partyId = party.partyId,
    electionId = electionId,
    elects = result.elects,
    votes = result.votes,
    party = party.toParty()
)

private fun PartyRaw.toParty() = Party(
    id = partyId,
    name = name,
    color = color
)

fun Election.toElectionWithResultsAndParty() = ElectionWithResultsAndParty(
    election = toElectionRaw(),
    results = results.map { it.toResultWithParty() }
)

private fun Election.toElectionRaw() = ElectionRaw(
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

private fun Result.toResultWithParty() = ResultWithParty(
    result = toResultRaw(),
    party = party.toPartyRaw()
)

private fun Result.toResultRaw() = ResultRaw(
    id = id,
    resultPartyId = partyId,
    resultElectionId = electionId,
    elects = elects,
    votes = votes
)

private fun Party.toPartyRaw() = PartyRaw(
    partyId = id,
    name = name,
    color = color
)
