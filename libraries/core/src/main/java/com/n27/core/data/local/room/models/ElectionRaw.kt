package com.n27.core.data.local.room.models

import androidx.room.Entity
import androidx.room.PrimaryKey

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
