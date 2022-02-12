package com.jorgedguezm.elections.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

import com.jorgedguezm.elections.models.Election

@Dao
interface ElectionDao {

    @Query("SELECT * FROM election WHERE place = :place AND chamberName = :chamber")
    suspend fun queryElections(place: String, chamber: String?): List<Election>

    @Query("SELECT * FROM election WHERE id = :id")
    suspend fun getElection(id: Long): Election

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertElection(election: Election)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertElections(elections: List<Election>)
}