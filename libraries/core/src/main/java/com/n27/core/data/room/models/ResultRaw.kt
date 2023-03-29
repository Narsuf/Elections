package com.n27.core.data.room.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "results")
data class ResultRaw(
    @PrimaryKey val id: Long,
    var resultPartyId: Long,
    var resultElectionId: Long,
    val elects: Int,
    val votes: Int
)
