package com.n27.core.data.room.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "parties")
data class PartyRaw(
    @PrimaryKey val partyId: Long,
    var name: String,
    val color: String
)
