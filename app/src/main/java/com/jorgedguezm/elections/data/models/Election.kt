package com.jorgedguezm.elections.data.models

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

        @Json(name = "chamberName")
        val chamberName: String,

        @Json(name = "totalElects")
        val totalElects: Int,

        @Json(name = "scrutinized")
        val scrutinized: Float,

        @Json(name = "validVotes")
        val validVotes: Int,

        @Json(name = "abstentions")
        val abstentions: Int,

        @Json(name = "blankVotes")
        val blankVotes: Int,

        @Json(name = "nullVotes")
        val nullVotes: Int,

        @Json(name = "results")
        val result: List<Results>
) : Serializable

data class Party(val name: String, val color: String) : Serializable
data class Results(val elects: Int, val votes: Int, val party: Party) : Serializable
