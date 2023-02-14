package com.jorgedguezm.elections.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "election")
data class Election(

        @PrimaryKey
        val id: Long = 0,
        val name: String = "",
        var date: String = "",
        val place: String = "",
        val chamberName: String = "",
        val totalElects: Int = 0,
        val scrutinized: Float = 0f,
        val validVotes: Int = 0,
        val abstentions: Int = 0,
        val blankVotes: Int = 0,
        val nullVotes: Int = 0,
        val results: List<Result> = listOf()
) : Serializable

data class Result(
        val elects: Int = 0,
        val votes: Int = 0,
        val party: Party = Party()
) : Serializable

data class Party(val name: String = "", val color: String = "") : Serializable
