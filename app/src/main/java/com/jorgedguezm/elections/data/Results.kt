package com.jorgedguezm.elections.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

import com.squareup.moshi.Json

import java.io.Serializable

@Entity(tableName = "results",
        foreignKeys = [
                ForeignKey(entity = Election::class, parentColumns = ["id"],
                        childColumns = ["electionId"], onDelete = ForeignKey.CASCADE)])
data class Results(

        @Json(name = "id")
        @PrimaryKey
        var id: Long,

        @Json(name = "elects")
        val elects: Int?,

        @Json(name = "votes")
        val votes: Int,

        @Json(name = "party")
        val party: Party,

        @Json(name="election")
        val electionId: Long
) : Serializable