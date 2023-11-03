package com.n27.test.generators

import com.n27.core.Constants.KEY_SENATE
import com.n27.core.domain.election.models.Election
import com.n27.core.domain.election.models.Elections
import com.n27.core.domain.election.models.Party
import com.n27.core.domain.election.models.Result

fun getElections() = Elections(getElectionList())

fun getGeneralElection() = Elections(listOf(getElection(), getElection(chamberName = KEY_SENATE)))

fun getElectionList(): List<Election> = listOf(getElection())

fun getElection(
    id: Long = 3,
    name: String = "Generales",
    date: String = "2015",
    place: String = "España",
    chamberName: String = "Congreso",
    totalElects: Int = 350,
    validVotes: Int = 25349824,
    abstentions: Int = 9280429,
    blankVotes: Int = 187766,
    nullVotes: Int = 226994,
    results: List<Result> = listOf(getResult())
) = Election(
    id,
    name,
    date,
    place,
    chamberName,
    totalElects,
    scrutinized = 100f,
    validVotes,
    abstentions,
    blankVotes,
    nullVotes,
    results
)

fun getResult(
    id: Long = 17,
    partyId: Long = 1,
    electionId: Long = 3,
    elects: Int = 123,
    votes: Int = 7215530,
    party: Party = getParty()
) = Result(id, partyId, electionId, elects, votes, party)
