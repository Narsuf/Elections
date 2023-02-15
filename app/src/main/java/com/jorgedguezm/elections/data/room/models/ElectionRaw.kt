package com.jorgedguezm.elections.data.room.models

import androidx.room.*
import androidx.room.ForeignKey.Companion.CASCADE

@Entity(tableName = "elections")
data class ElectionRaw(
    @PrimaryKey val id: Long,
    val name: String,
    var date: String,
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
    @PrimaryKey var id: Long,
    val name: String,
    val color: String
)

@Entity(
    tableName = "results",
    foreignKeys = [
        ForeignKey(
            entity = ElectionRaw::class,
            parentColumns = ["id"],
            childColumns = ["electionId"],
            onDelete = CASCADE
        ),
        ForeignKey(
            entity = PartyRaw::class,
            parentColumns = ["id"],
            childColumns = ["partyId"],
            onDelete = CASCADE
        )
    ],
    indices = [
        Index("electionId"),
        Index("partyId")
    ]
)
data class ResultRaw(
    @PrimaryKey var id: Long,
    var partyId: Long,
    var electionId: Long,
    val elects: Int,
    val votes: Int
)

@Entity
data class ResultWithParty(
    @Embedded val result: ResultRaw,
    @Relation(
        entity = PartyRaw::class,
        parentColumn = "partyId",
        entityColumn = "id"
    )
    val party: PartyRaw
)

data class ElectionWithResultsAndParty(
    @Embedded val election: ElectionRaw,
    @Relation(
        entity = ResultRaw::class,
        parentColumn = "id",
        entityColumn = "electionId"
    )
    val results: List<ResultWithParty>
)