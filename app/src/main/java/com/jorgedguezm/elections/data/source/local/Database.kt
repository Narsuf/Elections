package com.jorgedguezm.elections.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

import com.jorgedguezm.elections.data.Election
import com.jorgedguezm.elections.data.ResultsConverter

@Database(entities = [Election::class], version = 1)
@TypeConverters(ResultsConverter::class)
abstract class Database : RoomDatabase() {
    abstract fun electionsDao(): ElectionsDao
}