package com.jorgedguezm.elections.data

import androidx.room.TypeConverter

import com.google.gson.Gson

class ResultsConverter {

    @TypeConverter
    fun fromParty(value: String): Party? {
        return Gson().fromJson(value, Party::class.java)
    }

    @TypeConverter
    fun partyToString(p: Party): String {
        return Gson().toJson(p)
    }
}