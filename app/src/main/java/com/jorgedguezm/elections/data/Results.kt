package com.jorgedguezm.elections.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

import com.squareup.moshi.Json

import java.io.Serializable

@Entity(tableName = "results", foreignKeys = [
    ForeignKey(entity = Party::class, parentColumns = ["name"], childColumns = ["partyId"],
            onDelete = ForeignKey.CASCADE),
    ForeignKey(entity = Election::class, parentColumns = ["id"], childColumns = ["electionId"],
            onDelete = ForeignKey.CASCADE)])
data class Results(

        @Json(name = "elects")
        val elects: Int?,

        @Json(name = "votes")
        val votes: Int,

        @Json(name = "party")
        val partyId: String,

        @Json(name="election")
        val electionId: Long
) : Serializable {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}