package com.n27.test.generators

import com.n27.core.data.models.Election
import com.n27.core.data.models.Party
import com.n27.core.data.models.Result

fun getElections() = listOf(getElection())

fun getElection(chamberName: String = "Congreso") = Election(
    id = 3,
    name = "Generales",
    date = "2015",
    place = "España",
    chamberName = chamberName,
    totalElects = 350,
    scrutinized = 100f,
    validVotes = 25349824,
    abstentions = 9280429,
    blankVotes = 187766,
    nullVotes = 226994,
    results = listOf(getResult())
)

private fun getResult() = Result(
    id = 17,
    partyId = 1,
    electionId = 3,
    elects = 123,
    votes = 7215530,
    party = getParty()
)

private fun getParty() = Party(
    id = 1,
    name = "PP",
    color = "006EC7"
)
