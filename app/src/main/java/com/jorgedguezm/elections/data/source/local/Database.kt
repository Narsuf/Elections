package com.jorgedguezm.elections.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters

import com.jorgedguezm.elections.data.Election
import com.jorgedguezm.elections.data.Party
import com.jorgedguezm.elections.data.Results
import com.jorgedguezm.elections.data.ResultsConverter

@Database(entities = [Party::class, Election::class, Results::class], version = 1)
@TypeConverters(ResultsConverter::class)
abstract class Database : RoomDatabase() {
    abstract fun partiesDao(): PartiesDao
    abstract fun electionsDao(): ElectionsDao
    abstract fun resultsDao(): ResultsDao
}