package com.jorgedguezm.elections.models

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

        @Json(name = "date")
        var date: String,

        @Json(name = "place")
        val place: String,

        @Json(name = "chamber_name")
        val chamberName: String,

        @Json(name = "total_elects")
        val totalElects: Int,

        @Json(name = "scrutinized")
        val scrutinized: Float,

        @Json(name = "valid_votes")
        val validVotes: Int,

        @Json(name = "abstentions")
        val abstentions: Int,

        @Json(name = "blank_votes")
        val blankVotes: Int,

        @Json(name = "null_votes")
        val nullVotes: Int,

        @Json(name = "results")
        val results: List<Results>
) : Serializable

data class Party(val name: String, val color: String) : Serializable
data class Results(val elects: Int, val votes: Int, val party: Party) : Serializable