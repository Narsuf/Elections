package com.n27.test.generators

import com.n27.core.data.remote.api.models.ElDiarioParty
import com.n27.core.data.remote.api.models.ElDiarioPartyResult
import com.n27.core.data.remote.api.models.ElDiarioResult

fun getElDiarioResult(
    id: String = "04001",
    partiesResults: List<ElDiarioPartyResult> = listOf(getElDiarioPartyResult())
) = ElDiarioResult(
    id,
    date = 2305,
    abstentions = 215,
    blankVotes = 7,
    census = 1015,
    scrutinized = 10000,
    nullVotes = 19,
    validVotes = 800,
    seats = 9,
    partiesResults = partiesResults
)

fun getElDiarioPartyResult() = ElDiarioPartyResult(
    id = "0030",
    votes = 411,
    percentage = 5262,
    seats = 5
)

fun getElDiarioParties() = listOf(
    getElDiarioParty(),
    ElDiarioParty(id = "0006", name = "PP", color = "#02A2DD")
)

fun getElDiarioParty() = ElDiarioParty(
    id = "0030",
    name = "PSOE",
    color = "#E02020"
)