package com.n27.core.data.remote.api.mappers

import com.n27.core.data.remote.api.models.ElDiarioPartyResult
import org.json.JSONObject

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