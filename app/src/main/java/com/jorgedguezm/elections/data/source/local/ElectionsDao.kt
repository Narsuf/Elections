package com.jorgedguezm.elections.data.source.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

import com.jorgedguezm.elections.data.Election

import io.reactivex.Single

@Dao
interface ElectionsDao {

    @Query("SELECT * FROM election")
    fun queryElections(): Single<List<Election>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertElection(election: Election)
}