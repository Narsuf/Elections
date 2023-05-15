package com.n27.core.data.local.room.mappers

import com.n27.core.data.local.room.models.ElectionWithResultsAndParty
import com.n27.core.domain.models.Election
import com.n27.core.domain.models.Elections

fun List<ElectionWithResultsAndParty>.toElections() = Elections(items = toElectionList())

internal fun List<ElectionWithResultsAndParty>.toElectionList() = map { it.toElection() }

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

fun List<Election>.toElectionsWithResultsAndParty() = map { it.toElectionWithResultsAndParty() }

private fun Election.toElectionWithResultsAndParty() = ElectionWithResultsAndParty(
    election = toElectionRaw(),
    results = results.map { it.toResultWithParty() }
)
