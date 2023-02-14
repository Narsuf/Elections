package com.jorgedguezm.elections.data.room.converters

import androidx.room.TypeConverter

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

import com.jorgedguezm.elections.data.models.Result

class ResultsConverter {

    @TypeConverter
    fun fromResults(value: String): List<Result> {
        val type = object : TypeToken<List<Result>>() {}.type
        return Gson().fromJson(value, type)
    }

    @TypeConverter
    fun resultsToString(results: List<Result>): String {
        return Gson().toJson(results)
    }
}
