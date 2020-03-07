package com.jorgedguezm.elections.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

import com.jorgedguezm.elections.models.Election

@Dao
interface ElectionsDao {

    @Query("SELECT * FROM election WHERE place = :place")
    fun queryElections(place: String): List<Election>

    @Query("SELECT * FROM election WHERE place = :place AND chamberName = :chamber")
    fun queryChamberElections(place: String, chamber: String): List<Election>

    @Query("SELECT * FROM election WHERE id = :id")
    fun getElection(id: Long): Election

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertElection(election: Election)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertElections(elections: List<Election>)
}