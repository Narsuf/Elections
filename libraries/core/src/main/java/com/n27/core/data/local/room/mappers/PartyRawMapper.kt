package com.n27.core.data.local.room.mappers

import com.n27.core.data.local.room.models.PartyRaw
import com.n27.core.domain.election.models.Party

fun List<PartyRaw>.toParties() = map { it.toParty() }

internal fun PartyRaw.toParty() = Party(
    id = partyId,
    name = name,
    color = color
)

internal fun Party.toPartyRaw() = PartyRaw(
    partyId = id,
    name = name,
    color = color
)
