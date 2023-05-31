package com.n27.core.data.remote.eldiario.mappers

import com.n27.core.data.remote.eldiario.models.ElDiarioPartyResult
import com.n27.core.data.remote.eldiario.models.ElDiarioResult
import com.n27.core.domain.election.models.Election
import com.n27.core.domain.live.models.LiveElection
import org.json.JSONObject
import java.time.LocalDate
import java.time.format.DateTimeFormatter

fun JSONObject.getElDiarioPartiesResults(): List<ElDiarioPartyResult> {
    val parties = mutableListOf<ElDiarioPartyResult>()

    for (partyKey in keys()) {
        getJSONObject(partyKey)
            .toParty(partyKey)
            .takeIf { it.seats > 0 }
            ?.also { parties.add(it) }
    }

    return parties
}

private fun JSONObject.toParty(partyKey: String) = ElDiarioPartyResult(
    id = partyKey,
    votes = getInt("v"),
    percentage = getInt("p"),
    seats = getInt("s")
)