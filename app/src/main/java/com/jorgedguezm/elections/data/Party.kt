package com.jorgedguezm.elections.data

import androidx.room.Entity
import androidx.room.PrimaryKey

import com.squareup.moshi.Json

import java.io.Serializable

@Entity(tableName = "Party")
data class Party(

        @Json(name = "name")
        @PrimaryKey
        val id: String,

        @Json(name = "color")
        val name: String?
) : Serializable