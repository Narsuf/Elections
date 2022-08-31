package com.jorgedguezm.elections.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

import com.jorgedguezm.elections.data.models.Election

@Dao
interface ElectionDao {

    @Query("SELECT * FROM election WHERE place = :place")
    suspend fun queryElections(place: String): List<Election>

    @Query("SELECT * FROM election WHERE id = :id")
    suspend fun getElection(id: Long): Election

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertElection(election: Election)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertElections(elections: List<Election>)
}
