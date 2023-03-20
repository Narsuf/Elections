package com.n27.regional_live.data.api.mappers

import com.n27.core.data.models.Election
import com.n27.core.data.models.Party
import com.n27.core.data.models.Result
import com.n27.core.data.room.PartyRaw
import com.n27.regional_live.data.api.models.ElectionXml
import org.simpleframework.xml.core.Persister

internal fun String.toElection(parties: List<PartyRaw>) = Persister().read(ElectionXml::class.java, this)
    .toElection(parties)

internal fun ElectionXml.toElection(parties: List<PartyRaw>) = Election(
    id = 0,
    name = chamberName,
    date = year,
    place = place,
    chamberName = chamberName,
    totalElects = totalElects,
    scrutinized = scrutinized,
    validVotes = votes?.valid?.votes ?: 0,
    abstentions = votes?.abstentions?.votes ?: 0,
    blankVotes = votes?.blank?.votes ?: 0,
    nullVotes = votes?.notValid?.votes ?: 0,
    results = results?.parties?.map { it.toResult(parties) } ?: listOf(getEmptyResult())
)

private fun ElectionXml.Results.Party.toResult(parties: List<PartyRaw>) = Result(
    id = 0,
    partyId = id,
    electionId = 0,
    elects = elects,
    votes = votes,
    party = toParty(parties)
)

private fun ElectionXml.Results.Party.toParty(parties: List<PartyRaw>) = Party(
    id = id,
    name = name,
    color = getColor(parties)
)

private fun ElectionXml.Results.Party.getColor(parties: List<PartyRaw>) =
    parties.find { it.name == name }?.color
    ?: parties.find { name.contains(it.name) }?.color
    ?: String.format("%06x", (0..0xFFFFFF).random())

private fun getEmptyResult() = Result(
    id = 0,
    partyId = 0,
    electionId = 0,
    elects = 0,
    votes = 0,
    party = getEmptyParty()
)

private fun getEmptyParty() = Party(
    id = 0,
    name = "",
    color = ""
)
