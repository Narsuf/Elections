package com.n27.test.generators

import com.n27.core.data.room.*

fun getElectionWithResultsAndParty() = ElectionWithResultsAndParty(
    election = getElectionRaw(),
    results = listOf(getResultWithParty())
)

private fun getElectionRaw() = ElectionRaw(
    electionId = 3,
    name = "Generales",
    date = "2015",
    place = "España",
    chamberName = "Congreso",
    totalElects = 350,
    scrutinized = 100f,
    validVotes = 25349824,
    abstentions = 9280429,
    blankVotes = 187766,
    nullVotes = 226994
)

private fun getResultWithParty() = ResultWithParty(
    result = getResultRaw(),
    party = getPartyRaw()
)

private fun getResultRaw() = ResultRaw(
    id = 17,
    resultPartyId = 1,
    resultElectionId = 3,
    elects = 123,
    votes = 7215530
)

private fun getPartyRaw() = PartyRaw(
    partyId = 1,
    name = "PP",
    color = "006EC7"
)
