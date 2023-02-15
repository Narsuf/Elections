package com.jorgedguezm.elections.data.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ElectionRaw::class, PartyRaw::class, ResultRaw::class], version = 1,
    exportSchema = false)
abstract class Database : RoomDatabase() {
    abstract fun electionDao(): ElectionDao
}
