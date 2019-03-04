package com.jorgedguezm.elections.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

import com.squareup.moshi.Json

import java.io.Serializable

@Entity(tableName = "results")
data class Results(

        @Json(name = "id")
        @PrimaryKey
        val id: Long,

        @Json(name = "elects")
        val elects: Int?,

        @Json(name = "votes")
        val votes: Int,

        @Json(name = "party")
        @ForeignKey(entity = Party::class, parentColumns = ["name"], childColumns = ["partyId"],
                onDelete = ForeignKey.CASCADE)
        val partyId: String,

        @Json(name="election")
        @ForeignKey(entity = Election::class, parentColumns = ["id"], childColumns = ["electionId"],
                onDelete = ForeignKey.CASCADE)
        val electionId: Long
) : Serializable