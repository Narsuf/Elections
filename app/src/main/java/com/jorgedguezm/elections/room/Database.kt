package com.jorgedguezm.elections.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

import com.jorgedguezm.elections.models.Election
import com.jorgedguezm.elections.room.converters.ResultsConverter

@Database(entities = [Election::class], version = 1)
@TypeConverters(ResultsConverter::class)
abstract class Database : RoomDatabase() {
    abstract fun electionsDao(): ElectionsDao
}