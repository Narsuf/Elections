package com.n27.core.data.api.mappers

import com.n27.core.data.api.models.ElectionXml
import com.n27.core.data.api.models.PartyXml
import com.n27.core.data.api.models.ResultsXml
import com.n27.core.data.models.Election
import com.n27.core.data.models.Party
import com.n27.core.data.models.Result
import com.n27.core.data.room.models.PartyRaw
import org.simpleframework.xml.core.Persister

internal fun String.toElection(parties: List<PartyRaw>) = toElectionXml().toElection(parties)

internal fun String.toElectionXml(electionId: String? = null): ElectionXml = Persister()
    .read(ElectionXml::class.java, this)
    .apply { electionId?.let { id = it } }

fun ElectionXml.toElection(parties: List<PartyRaw>) = Election(
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
