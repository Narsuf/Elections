package com.jorgedguezm.elections.data.room.converters

import androidx.room.TypeConverter

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

import com.jorgedguezm.elections.data.models.Results

class ResultsConverter {

    @TypeConverter
    fun fromResults(value: String): List<Results> {
        val type = object : TypeToken<List<Results>>() {}.type
        return Gson().fromJson(value, type)
    }

    @TypeConverter
    fun resultsToString(results: List<Results>): String {
        return Gson().toJson(results)
    }
}
