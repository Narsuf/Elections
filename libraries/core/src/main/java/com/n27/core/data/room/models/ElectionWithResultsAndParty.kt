package com.n27.core.data.room.models

import androidx.room.Embedded
import androidx.room.Relation

data class ElectionWithResultsAndParty(
    @Embedded val election: ElectionRaw,
    @Relation(
        entity = ResultRaw::class,
        parentColumn = "electionId",
        entityColumn = "resultElectionId"
    )
    val results: List<ResultWithParty>
)
