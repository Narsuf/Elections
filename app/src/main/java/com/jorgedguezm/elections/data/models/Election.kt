package com.jorgedguezm.elections.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

import com.squareup.moshi.Json

import java.io.Serializable

@Entity(tableName = "election")
data class Election(

        @Json(name = "id")
        @PrimaryKey
        val id: Long = 0,

        @Json(name = "name")
        val name: String = "",

        @Json(name = "date")
        var date: String = "",

        @Json(name = "place")
        val place: String = "",

        @Json(name = "chamberName")
        val chamberName: String = "",

        @Json(name = "totalElects")
        val totalElects: Int = 0,

        @Json(name = "scrutinized")
        val scrutinized: Float = 0f,

        @Json(name = "validVotes")
        val validVotes: Int = 0,

        @Json(name = "abstentions")
        val abstentions: Int = 0,

        @Json(name = "blankVotes")
        val blankVotes: Int = 0,

        @Json(name = "nullVotes")
        val nullVotes: Int = 0,

        @Json(name = "results")
        val results: List<Result> = listOf()
) : Serializable
data class Result(
        val elects: Int = 0,
        val votes: Int = 0,
        val party: Party = Party()
) : Serializable

data class Party(val name: String = "", val color: String = "") : Serializable
