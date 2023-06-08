package com.n27.core.data.remote.api.mappers

import com.n27.core.data.remote.api.models.ElDiarioParty
import com.n27.core.data.remote.api.models.ElDiarioPartyResult
import com.n27.core.data.remote.api.models.ElDiarioResult
import com.n27.core.domain.election.models.Election
import com.n27.core.domain.election.models.Party
import com.n27.core.domain.election.models.Result
import com.n27.core.domain.live.models.LiveElection
import com.n27.core.extensions.sortResultsByElectsAndVotes
import org.json.JSONObject

fun String.toElDiarioResult(date: Long, seats: Int): ElDiarioResult = JSONObject(this)
    .run { getJSONObject("9999") }
    .toElDiarioResult(date, seats = seats)

fun String.toElDiarioRegionalResult(id: String, date: Long): ElDiarioResult = JSONObject(this)
    .run { getJSONObject(keys().next()) }
    .toElDiarioResult(date, id)

fun String.toElDiarioLocalResult(id: String, date: Long): ElDiarioResult = JSONObject(this)
    .run { getJSONObject(id) }
    .toElDiarioResult(date, id)

private fun JSONObject.toElDiarioResult(date: Long, id: String = "", seats: Int? = null): ElDiarioResult = getJSONObject("i")
    .let { info ->
        ElDiarioResult(
            id = id,
            date = date,
            abstentions = info.getInt("abst"),
            blankVotes = info.getInt("bl"),
            census = info.getInt("censo"),
            scrutinized = info.getInt("escrutado"),
            nullVotes = info.getInt("nl"),
            validVotes = info.getInt("ok"),
            seats = seats ?: info.getInt("seats"),
            partiesResults = getJSONObject("v").getElDiarioPartiesResults()
        )
    }

fun ElDiarioResult.toLiveElection(name: String, place: String, parties: List<ElDiarioParty>) = LiveElection(
    id = id,
    election = toElection(name, place, parties).sortResultsByElectsAndVotes()
)

private fun ElDiarioResult.toElection(name: String, place: String, parties: List<ElDiarioParty>) = Election(
    id = 0,
    name = name,
    date = date.toString().toDate(),
    place = place,
    chamberName = "",
    totalElects = seats,
    scrutinized = scrutinized / 100f,
    validVotes = validVotes,
    abstentions = abstentions,
    blankVotes = blankVotes,
    nullVotes = nullVotes,
    results = partiesResults.map { it.toResult(parties) }
)

private fun String.toDate() = "${substring(2)}/${substring(0, 2)}"

private fun ElDiarioPartyResult.toResult(parties: List<ElDiarioParty>) = Result(
    id = 0,
    partyId = id.toLong(),
    electionId = 0,
    elects = seats,
    votes = votes,
    party = parties
        .first { it.id == id }
        .toParty()
)

private fun ElDiarioParty.toParty() = Party(
    id = id.toLong(),
    name = name,
    color = color
)
