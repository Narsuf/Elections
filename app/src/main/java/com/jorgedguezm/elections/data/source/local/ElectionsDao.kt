package com.jorgedguezm.elections.data.source.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

import com.jorgedguezm.elections.data.Election

import io.reactivex.Single

@Dao
interface ElectionsDao {

    @Query("SELECT * FROM election WHERE place = :place AND chamberName = :chamber")
    fun queryElections(place: String, chamber: String): Single<List<Election>>

    @Query("SELECT * FROM election WHERE id = :id")
    fun getElection(id: Long): Single<Election>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertElection(election: Election)
}