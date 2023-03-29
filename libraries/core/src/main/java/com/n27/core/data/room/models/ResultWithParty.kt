package com.n27.core.data.room.models

import androidx.room.Embedded
import androidx.room.Relation

data class ResultWithParty(
    @Embedded val result: ResultRaw,
    @Relation(
        entity = PartyRaw::class,
        parentColumn = "resultPartyId",
        entityColumn = "partyId"
    )
    val party: PartyRaw
)
