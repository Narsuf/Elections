package com.n27.core.data.remote.api.mappers

import com.n27.core.data.remote.api.models.ElectionXml
import com.n27.core.domain.election.models.Election
import com.n27.core.domain.election.models.Party
import com.n27.core.domain.election.models.Result
import com.n27.core.domain.live.models.LiveElection
import com.n27.core.domain.live.models.LiveElections
import org.simpleframework.xml.core.Persister

fun String.toElectionXml(electionId: String? = null): ElectionXml = Persister()
    .read(ElectionXml::class.java, this)
    .apply { electionId?.let { id = it } }

fun List<ElectionXml>.toLiveElections(parties: List<Party>) = LiveElections(
    items = map { it.toLiveElection(parties) }
)

fun ElectionXml.toLiveElection(parties: List<Party>) = LiveElection(
    id = id,
    election = toElection(parties)
)

fun ElectionXml.toElection(parties: List<Party>) = Election(
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

private fun getEmptyResult() = Result(
    id = 0,
    partyId = 0,
    electionId = 0,
    elects = 0,
    votes = 0,
    party = getEmptyParty()
)
