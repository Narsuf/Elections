package com.n27.core.data.room.models

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(tableName = "elections")
data class ElectionRaw(
    @PrimaryKey val electionId: Long,
    val name: String,
    val date: String,
    val place: String,
    val chamberName: String,
    val totalElects: Int,
    val scrutinized: Float,
    val validVotes: Int,
    val abstentions: Int,
    val blankVotes: Int,
    val nullVotes: Int,
)

@Entity(tableName = "parties")
data class PartyRaw(
    @PrimaryKey val partyId: Long,
    var name: String,
    val color: String
)

@Entity(tableName = "results")
data class ResultRaw(
    @PrimaryKey val id: Long,
    var resultPartyId: Long,
    var resultElectionId: Long,
    val elects: Int,
    val votes: Int
)

data class ResultWithParty(
    @Embedded val result: ResultRaw,
    @Relation(
        entity = PartyRaw::class,
        parentColumn = "resultPartyId",
        entityColumn = "partyId"
    )
    val party: PartyRaw
)

data class ElectionWithResultsAndParty(
    @Embedded val election: ElectionRaw,
    @Relation(
        entity = ResultRaw::class,
        parentColumn = "electionId",
        entityColumn = "resultElectionId"
    )
    val results: List<ResultWithParty>
)
