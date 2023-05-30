package com.n27.core.data.remote.eldiario.mappers

import com.n27.core.data.remote.eldiario.models.ElDiarioParty
import org.json.JSONObject

fun String.toElDiarioParties(): List<ElDiarioParty> = JSONObject(this).getParties()

private fun JSONObject.getParties(): List<ElDiarioParty> {
    val parties = mutableListOf<ElDiarioParty>()

    for (key in keys()) parties.add(getJSONObject(key).toParty())

    return parties
}

private fun JSONObject.toParty() = ElDiarioParty(
    id = getString("group"),
    name = getString("sigla"),
    color = getString("color")
)