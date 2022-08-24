package com.jorgedguezm.elections.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

import com.jorgedguezm.elections.data.models.Election
import com.jorgedguezm.elections.data.room.converters.ResultsConverter

@Database(entities = [Election::class], version = 1, exportSchema = false)
@TypeConverters(ResultsConverter::class)
abstract class Database : RoomDatabase() {
    abstract fun electionDao(): ElectionDao
}
