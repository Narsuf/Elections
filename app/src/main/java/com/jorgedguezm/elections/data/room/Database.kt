package com.jorgedguezm.elections.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.jorgedguezm.elections.data.room.models.ElectionRaw
import com.jorgedguezm.elections.data.room.models.PartyRaw
import com.jorgedguezm.elections.data.room.models.ResultRaw

@Database(entities = [ElectionRaw::class, PartyRaw::class, ResultRaw::class], version = 1,
    exportSchema = false)
abstract class Database : RoomDatabase() {
    abstract fun electionDao(): ElectionDao
}
