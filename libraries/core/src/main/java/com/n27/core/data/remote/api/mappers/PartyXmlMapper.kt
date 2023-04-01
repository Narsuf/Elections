package com.n27.core.data.remote.api.mappers

import com.n27.core.data.local.room.models.PartyRaw
import com.n27.core.data.models.Party
import com.n27.core.data.models.Result
import com.n27.core.data.remote.api.models.PartyXml

internal fun getEmptyParty() = Party(
    id = 0,
    name = "",
    color = ""
)

internal fun PartyXml.toResult(parties: List<PartyRaw>) = Result(
    id = 0,
    partyId = id,
    electionId = 0,
    elects = elects,
    votes = votes,
    party = toParty(parties)
)

private fun PartyXml.toParty(parties: List<PartyRaw>) = Party(
    id = id,
    name = name,
    color = getColor(parties)
)

private fun PartyXml.getColor(parties: List<PartyRaw>) =
    parties.find { it.name == name.lowercase() }?.color
        ?: parties.find { name.lowercase().contains(it.name) }?.color
        ?: String.format("%06x", (0..0xFFFFFF).random())
