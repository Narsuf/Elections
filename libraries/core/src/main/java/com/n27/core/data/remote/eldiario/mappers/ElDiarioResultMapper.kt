package com.n27.core.data.remote.eldiario.mappers

import com.n27.core.data.remote.eldiario.models.ElDiarioResult
import org.json.JSONObject

fun String.toElDiarioResult(): ElDiarioResult = JSONObject(this)
    .run { getJSONObject(keys().next()) }
    .getElDiarioResult()

private fun JSONObject.getElDiarioResult(): ElDiarioResult = getJSONObject("i").let { info ->
    ElDiarioResult(
        abstentions = info.getInt("abst"),
        blankVotes = info.getInt("bl"),
        census = info.getInt("censo"),
        scrutinized = info.getInt("escrutado"),
        nullVotes = info.getInt("nl"),
        validVotes = info.getInt("ok"),
        seats = info.getInt("seats"),
        parties = getJSONObject("v").getParties()
    )
}

private fun JSONObject.getParties(): List<ElDiarioResult.ElDiarioPartyResult> {
    val parties = mutableListOf<ElDiarioResult.ElDiarioPartyResult>()

    for (partyKey in keys()) {
        getJSONObject(partyKey)
            .toParty(partyKey)
            .takeIf { it.seats > 0 }
            ?.also { parties.add(it) }
    }

    return parties
}

private fun JSONObject.toParty(partyKey: String) = ElDiarioResult.ElDiarioPartyResult(
    id = partyKey,
    votes = getInt("v"),
    percentage = getInt("p"),
    seats = getInt("s")
)