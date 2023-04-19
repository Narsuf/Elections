package com.n27.core.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.n27.core.data.local.room.models.ElectionRaw
import com.n27.core.data.local.room.models.PartyRaw
import com.n27.core.data.local.room.models.ResultRaw

@Database(entities = [ElectionRaw::class, PartyRaw::class, ResultRaw::class], version = 1, exportSchema = false)
abstract class Database : RoomDatabase() {
    abstract fun electionDao(): ElectionDao
}
