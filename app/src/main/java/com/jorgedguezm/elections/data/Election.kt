package com.jorgedguezm.elections.data

import androidx.room.Entity
import androidx.room.PrimaryKey

import com.squareup.moshi.Json

import java.io.Serializable

@Entity(tableName = "election")
data class Election(

        @Json(name = "id")
        @PrimaryKey
        val id: Long,

        @Json(name = "name")
        val name: String,

        @Json(name = "year")
        val year: Int,

        @Json(name = "place")
        val place: String,

        @Json(name = "chamber_name")
        val chamberName: String,

        @Json(name = "total_elects")
        val totalElects: Int?,

        @Json(name = "scrutinized")
        val scrutinized: Float?,

        @Json(name = "valid_votes")
        val validVotes: Int,

        @Json(name = "abstentions")
        val abstentions: Int?,

        @Json(name = "blank_votes")
        val blankVotes: Int?,

        @Json(name = "null_votes")
        val nullVotes: Int?
) : Serializable